package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.persistence.PreUpdate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInValidException;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("resumes")
    @ApiMessage("create a resume")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInValidException {
        boolean isIdExit = this.resumeService.checkResumeExitByUserAndJob(resume);
        if(!isIdExit) {
            throw new IdInValidException("User id/Job id invalid");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
    }

    @PutMapping("resumes")
    @ApiMessage("update resume")
    public ResponseEntity<ResUpdateResumeDTO> update( @RequestBody Resume resume) throws IdInValidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchById(resume.getId());
        if(!resumeOptional.isPresent()) {
            throw new IdInValidException("User id/Job id invalid");
        }
        Resume resumeToUpdate = resumeOptional.get();
        resumeToUpdate.setStatus(resume.getStatus());
        return ResponseEntity.ok().body(this.resumeService.update(resumeToUpdate));

    }

    @DeleteMapping("resumes/{id}")
    @ApiMessage("delete resume")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInValidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if(!resumeOptional.isPresent()) {
            throw new IdInValidException("User id/Job id invalid");
        }
        this.resumeService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("resumes/{id}")
    @ApiMessage("fetch a resume by id")
    public ResponseEntity<ResFetchResumeDTO> get(@PathVariable("id") long id) throws IdInValidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if(!resumeOptional.isPresent()) {
            throw new IdInValidException("User id/Job id invalid");
        }
        return ResponseEntity.ok().body(this.resumeService.getResume(resumeOptional.get()));
    }

    @GetMapping("resumes")
    @ApiMessage("fetch all resume with paginate")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
            @Filter Specification<Resume> spec,
            Pageable pageable
            ) {
        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec, pageable));
    }


}
