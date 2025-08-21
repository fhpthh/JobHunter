package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.*;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company company) {
        return companyRepository.save(company);
    }

    public ResultPaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> comPage = companyRepository.findAll(spec,pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(comPage.getTotalPages());
        meta.setTotal(comPage.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(comPage.getContent());
        return rs;
    }

    public Company handleUpdateCompany(Company company) {
        Optional<Company> companyOptional = companyRepository.findById(company.getId());
        if (companyOptional.isPresent()) {
            Company companyToUpdate = companyOptional.get();
            companyToUpdate.setName(company.getName());
            companyToUpdate.setLogo(company.getLogo());
            companyToUpdate.setDescription(company.getDescription());
            companyToUpdate.setAddress(company.getAddress());

            return companyRepository.save(companyToUpdate);

        }
        return null;
    }

    public void handleDeleteCompany(Long id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            List<User> users = this.userRepository.findByCompany(company);
            this.userRepository.deleteAll(users);
        }
        this.companyRepository.deleteById(id);
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

}
