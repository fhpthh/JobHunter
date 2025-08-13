package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.*;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    done vid 72 lay ra list va query
//    public List<User> findAll(Pageable pageable) {
//        Page<User> users = userRepository.findAll(pageable);
//        return users.getContent();
//    }
    public ResultPaginationDTO findAll(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
//
        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent()
                        .stream().map(item -> new ResUserDTO(
                            item.getId(),
                            item.getEmail(),
                            item.getName(),
                            item.getAge(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getAddress(),
                        item.getGender()))
                        .collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }
    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }
    public User handleUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.handleUserById(user.getId());

        if (currentUser != null) {
            currentUser.setName(user.getName());
            currentUser.setAddress(user.getAddress());
            currentUser.setGender(user.getGender());
            currentUser.setAge(user.getAge());

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean checkEmail(String email) {
        return this.userRepository.existsUserByEmail(email);
    }

    public ResCreateUserDTO convertTokenCreateUserDTO(User user) {
        ResCreateUserDTO rs = new ResCreateUserDTO();
        rs.setId(user.getId());
        rs.setEmail(user.getEmail());
        rs.setName(user.getName());
        rs.setAge(user.getAge());
        rs.setCreatedAt(user.getCreatedAt());
        rs.setAddress(user.getAddress());
        rs.setGender(user.getGender());
        return rs;
    }

    public ResUserDTO convertUserDTO(User user) {
        ResUserDTO rs = new ResUserDTO();
        rs.setId(user.getId());
        rs.setEmail(user.getEmail());
        rs.setName(user.getName());
        rs.setAge(user.getAge());
        rs.setCreatedAt(user.getCreatedAt());
        rs.setUpdatedAt(user.getUpdatedAt());
        rs.setAddress(user.getAddress());
        rs.setGender(user.getGender());
        return rs;

    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user){
        ResUpdateUserDTO rs = new ResUpdateUserDTO();
        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setAge(user.getAge());
        rs.setUpdatedAt(user.getUpdatedAt());
        rs.setGender(user.getGender());
        rs.setAddress(user.getAddress());
        return rs;



    }

    public void  updateUserToken(String token, String email) {
        User curUser = this.handleGetUserByUsername(email);

        if (curUser != null) {
            curUser.setRefreshToken(token);
            this.userRepository.save(curUser);
        }
    }


}
