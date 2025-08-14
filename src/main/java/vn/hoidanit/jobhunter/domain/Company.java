package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    @NotBlank(message = "name khong duoc trong")
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String address;
    private String logo;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleCreatedAt() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get() : "";
    }

    @PreUpdate
    public void handleUpdatedAt() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }


}
