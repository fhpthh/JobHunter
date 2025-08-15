package vn.hoidanit.jobhunter.domain.request;

import jakarta.validation.constraints.NotBlank;


public class ReqLoginDTO {

    @NotBlank(message = "username khong duoc de trong")
    private String username;
    @NotBlank(message = "Emil khong duoc de trong")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
