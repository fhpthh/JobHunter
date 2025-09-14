package vn.hoidanit.jobhunter.domain.response.resume;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Resume;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateResumeDTO {
    private Instant updateAt;
    private String updateBy;
}
