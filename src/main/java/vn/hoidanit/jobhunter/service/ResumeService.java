package vn.hoidanit.jobhunter.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRespository;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    private final ResumeRespository resumeRespository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    public ResumeService(ResumeRespository resumeRespository, JobRepository jobRepository, UserRepository userRepository) {
        this.resumeRespository = resumeRespository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;

    }

    public Optional<Resume> fetchById(long id) {
        return this.resumeRespository.findById(id);
    }

    public boolean checkResumeExitByUserAndJob(@Valid Resume resume) {
        if(resume.getUser() == null || resume.getJob() == null) {
            return false;
        }
        Optional<User> user = userRepository.findById(resume.getUser().getId());
        if(user.isEmpty()) {
            return false;
        }
        Optional<Job> job = jobRepository.findById(resume.getJob().getId());
        if(job.isEmpty()) {

            return false;
        }
        return true;

    }

    public ResCreateResumeDTO create(Resume resume) {
        resume = this.resumeRespository.save(resume);
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resume.getId());
        resCreateResumeDTO.setCreatedAt(resume.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resume.getCreatedBy());
        return resCreateResumeDTO;

    }

    public ResUpdateResumeDTO update(Resume resume) {
        resume = this.resumeRespository.save(resume);
        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdateBy(resume.getUpdatedBy());
        res.setUpdateAt(resume.getUpdatedAt());
        return res;
    }

    public void delete(long id) {
        this.resumeRespository.deleteById(id);
    }


    public ResFetchResumeDTO getResume(Resume resume) {
        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());

        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));

        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
        return res;
    }

    public ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> page = resumeRespository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(page.getTotalPages());
        mt.setTotal(page.getTotalElements());
        res.setMeta(mt);

        List<ResFetchResumeDTO> listResume = page.getContent()
                .stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());
        res.setResult(listResume);
        return res;
    }
}
