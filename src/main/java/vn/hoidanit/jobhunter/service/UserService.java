package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

//    done vid 72 lay ra list va query
//    public List<User> findAll(Pageable pageable) {
//        Page<User> users = userRepository.findAll(pageable);
//        return users.getContent();
//    }
    public ResultPaginationDTO findAll(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

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
                item.getGender(),
                new ResUserDTO.CompanyUser(
                    item.getCompany() != null ? item.getCompany().getId() : 0,
                    item.getCompany() != null ? item.getCompany().getName() : null
                )))
                .collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }

    public User handleCreateUser(User user) {
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);

        }
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

            if (user.getCompany() != null) {
                Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
                currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
            }

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
        ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();

        rs.setId(user.getId());
        rs.setEmail(user.getEmail());
        rs.setName(user.getName());
        rs.setAge(user.getAge());
        rs.setCreatedAt(user.getCreatedAt());
        rs.setAddress(user.getAddress());
        rs.setGender(user.getGender());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            rs.setCompanyUser(com);
        }
        return rs;
    }

    public ResUserDTO convertUserDTO(User user) {
        ResUserDTO rs = new ResUserDTO();
        ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            rs.setCompanyUser(com);
        }
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

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO rs = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();
        if(user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            rs.setCompanyUser(com);
        }
        
        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setAge(user.getAge());
        rs.setUpdatedAt(user.getUpdatedAt());
        rs.setGender(user.getGender());
        rs.setAddress(user.getAddress());
        return rs;

    }

    public void updateUserToken(String token, String email) {
        User curUser = this.handleGetUserByUsername(email);

        if (curUser != null) {
            curUser.setRefreshToken(token);
            this.userRepository.save(curUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

}
