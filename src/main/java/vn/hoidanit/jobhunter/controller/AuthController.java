package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private  final SecurityUtil securityUtil;
    private final UserService userService;
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                          UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }
    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    public long refreshTokenExpiration;
    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid  @RequestBody LoginDTO loginDto) {
        // nap input gom username / pass vap security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        // xac thuc nguoi dung => viet ham loadByUserNam
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // set thong tin nguoi dung dang nhap vao context (co the su dung sau nay_
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(), currentUserDB.getName());
            resLoginDTO.setUser(userLogin);

        }
        String accessToken = this.securityUtil.createAccessToken(authentication, resLoginDTO.getUser());
        resLoginDTO.setAccess_token(accessToken);

        // create redresh token
        String refreshToken = this.securityUtil.createRefreshToken(loginDto.getUsername(),resLoginDTO );

        // update userr
        this.userService.updateUserToken(refreshToken, loginDto.getUsername());

        // set ccokies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refreshToken)
                .path("/") // setPath
                .httpOnly(true)
                .secure(true)   // nếu dùng HTTPS
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account message")
    public  ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
        }

        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<String> getRefreshToken(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();
        return ResponseEntity.ok().body(email);
    }


}
