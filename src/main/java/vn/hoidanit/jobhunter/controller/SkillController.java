package vn.hoidanit.jobhunter.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInValidException;

@RestController
@RequestMapping("api/v1")
public class SkillController {

    private final SkillService skillService;
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("skills")
    @ApiMessage("create a skill")
    public ResponseEntity<Skill> createSkill(@RequestBody @Valid Skill skill) throws IdInValidException {
        if(skill.getName() != null && this.skillService.isNameExist(skill.getName())) {
            throw new IdInValidException("Skill name = " + skill.getName() + "da ton tai");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(skill));
    }

    @PutMapping("skills")
    @ApiMessage("update a skill")
    public ResponseEntity<Skill> updateSkill(@RequestBody @Valid Skill skill) throws IdInValidException {

        Skill curSkill = this.skillService.fetchSkillById(skill.getId());
        if(curSkill == null) {
            throw new IdInValidException("Skill id = " + skill.getId() + "khong ton tai");
        }
        if(skill.getName() != null && this.skillService.isNameExist(skill.getName())) {
            throw new IdInValidException("Skill name = " + skill.getName() + "da ton tai");
        }
        curSkill.setName(skill.getName());
        return ResponseEntity.ok().body(this.skillService.updateSkill(skill));
    }

    @GetMapping("skills")
    @ApiMessage("fetch all skill")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkills(spec, pageable));

    }

    @DeleteMapping("skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Skill> deleteSkill(@PathVariable Long id) throws IdInValidException {
        Skill curSkill = this.skillService.fetchSkillById(id);
        if(curSkill == null) {
            throw new IdInValidException("Skill id = " + id + " khong ton ta");
        }
        this.skillService.deleteSkillById(id);
        return ResponseEntity.ok().body(null);
    }


}
