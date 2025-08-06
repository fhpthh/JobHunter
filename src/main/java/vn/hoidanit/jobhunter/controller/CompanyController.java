package vn.hoidanit.jobhunter.controller;

import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping ("/companies")
    public ResponseEntity<Company> createCompanies(@Valid @RequestBody Company company) {
        Company res = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @RequestParam("current") Optional<String> currentOp,
            @RequestParam("pageSize") Optional<String> pageSize
    ) {

         String sCurrentOtp = currentOp.isPresent() ? currentOp.get() : "";
         String sPageSize = pageSize.isPresent() ? pageSize.get() : "";

         int cur = Integer.parseInt(sCurrentOtp);
         int ps = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(cur - 1, ps);

        return ResponseEntity.ok().body(this.companyService.getAllCompanies(pageable));
    }

    @PutMapping("/compaines")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        Company updateCom = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.ok().body(updateCom);

    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable("id") Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
