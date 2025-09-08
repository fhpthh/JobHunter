package vn.hoidanit.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
public class ResUpdateJobDTO {
    private long id;
    private String name;
    private double salary;
    private int quantity;
    private String location;
    private LevelEnum level;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private String createdBy;
    private String updatedBy;
    private List<String> skill;


}
