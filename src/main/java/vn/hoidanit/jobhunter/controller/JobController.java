package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class JobController {

    private final JobService jobService;
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("create a job")
    public ResponseEntity<ResCreateJobDTO> create(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.create(job));
    }
    @PutMapping("/jobs")
    @ApiMessage("update a job")
    public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job) {
        Optional<Job> curJob = this.jobService.findById(job.getId());
        if (!curJob.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
        }

        return ResponseEntity.ok().body(this.jobService.update(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Job> curJob = this.jobService.findById(id);
        if (!curJob.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
        }
        this.jobService.deleteJob(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get a job by id")
    public ResponseEntity<Job> getById(@PathVariable Long id) {
        Optional<Job> curJob = this.jobService.findById(id);
        if (!curJob.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
        }
        return ResponseEntity.ok().body(curJob.get());

    }

    @GetMapping("/jobs")
    @ApiMessage("Get job with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
            @Filter
            Specification<Job> spec,
            Pageable pageable
            ) {
        return ResponseEntity.ok().body(this.jobService.fetchAll(spec, pageable));
    }



}
