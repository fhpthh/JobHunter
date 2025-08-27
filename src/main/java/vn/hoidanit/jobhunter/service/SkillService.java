package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRespository;

import java.util.Optional;

@Service
public class SkillService {

    private final SkillRespository skillRespository;
    public SkillService(SkillRespository skillRespository) {
        this.skillRespository = skillRespository;
    }

    public boolean isNameExist(String name) {
        return this.skillRespository.existsByName(name);
    }

    public Skill createSkill(Skill skill) {
        return this.skillRespository.save(skill);
    }

    public Skill fetchSkillById(Long id) {
        Optional<Skill> skill = this.skillRespository.findById(id);
        if (skill.isPresent()) {
            return skill.get();
        }
        return null;
    }

    public Skill updateSkill(Skill skill) {
        return this.skillRespository.save(skill);
    }

    public ResultPaginationDTO fetchAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRespository.findAll(spec, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        res.setMeta(mt);
        res.setResult(pageSkill.getContent());
        return res;
    }

    public void deleteSkillById(Long id) {

        // delete job (inside job_skill table
        Optional<Skill> skill = this.skillRespository.findById(id);
        Skill curSkill = skill.get();
        curSkill.getJobs().forEach(job -> job.getSkills().remove(curSkill));

        this.skillRespository.delete(curSkill);

    }

}
