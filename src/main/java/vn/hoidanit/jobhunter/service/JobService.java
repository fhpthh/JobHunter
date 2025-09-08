package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRespository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRespository skillRespository;
    public JobService(JobRepository jobRepository, SkillRespository skillRespository) {
        this.jobRepository = jobRepository;
        this.skillRespository = skillRespository;
    }
    public void deleteJob(Long id) {
        this.jobRepository.deleteById(id);
    }
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }
    public ResCreateJobDTO create(Job j) {
        if(j.getSkills() != null ) {
            List<Long> reqSkills = j.getSkills().stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill>  dbSkills = this.skillRespository.findByIdIn(reqSkills);
            j.setSkills(dbSkills);
        }

        Job currentJob = this.jobRepository.save(j);

        ResCreateJobDTO resCreateJobDTO = new ResCreateJobDTO();
        resCreateJobDTO.setId(currentJob.getId());
        resCreateJobDTO.setName(currentJob.getName());
        resCreateJobDTO.setSalary(currentJob.getSalary());
        resCreateJobDTO.setQuantity(currentJob.getQuantity());
        resCreateJobDTO.setLocation(currentJob.getLocation());
        resCreateJobDTO.setLevel(currentJob.getLevel());
        resCreateJobDTO.setStartDate(currentJob.getStartDate());
        resCreateJobDTO.setEndDate(currentJob.getEndDate());
        resCreateJobDTO.setActive(currentJob.isActive());
        resCreateJobDTO.setCreatedBy(currentJob.getCreatedBy());
        resCreateJobDTO.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());

            resCreateJobDTO.setSkill(skills);

        }
        return resCreateJobDTO;
    }

    public ResUpdateJobDTO update(Job j) {
        if(j.getSkills() != null ) {
            List<Long> reqSkills = j.getSkills().stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill>  dbSkills = this.skillRespository.findByIdIn(reqSkills);
            j.setSkills(dbSkills);
        }

        Job currentJob = this.jobRepository.save(j);

        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedBy(currentJob.getCreatedBy());
        dto.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());

            dto.setSkill(skills);

        }
        return dto;
    }

    public ResultPaginationDTO fetchAll(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageJob.getTotalElements());
        mt.setPages(pageJob.getTotalPages());

        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());
        return rs;

    }

}
