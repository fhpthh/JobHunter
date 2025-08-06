package vn.hoidanit.jobhunter.domain.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
}
