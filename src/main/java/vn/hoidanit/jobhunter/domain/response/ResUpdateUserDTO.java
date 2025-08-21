package vn.hoidanit.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private int age;
    private Instant updatedAt;
    private GenderEnum gender;
    private String address;

    private CompanyUser companyUser;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
