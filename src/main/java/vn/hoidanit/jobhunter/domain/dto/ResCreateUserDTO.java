package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private  long id;
    private String email;
    private String name;
    private int age;
    private Instant createdAt;
    private String address;
    private GenderEnum gender;
}
