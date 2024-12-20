package com.project.CompanyService.service;

import com.project.CompanyService.dto.CompanyDto;
import com.project.CompanyService.entity.Company;
import com.project.CompanyService.exceptions.CompanyAlreadyExistException;
import com.project.CompanyService.exceptions.CompanyNotFoundException;
import com.project.CompanyService.feignClient.UserCompanyFeignClient;
import com.project.CompanyService.mapper.CompanyMapper;
import com.project.CompanyService.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserCompanyFeignClient userCompanyFeignClient;

    @Override
    public CompanyDto addCompany(CompanyDto companyDto) {

        if(companyRepository.findByName(companyDto.getName()).isPresent()) {
            throw new CompanyAlreadyExistException(companyDto.getName());
        }
        Company company = companyMapper.toEntity(companyDto);
        company.setId(UUID.randomUUID().toString());
        company.setIdUser(userCompanyFeignClient.currentUser().getBody().getIdUser());

        companyRepository.save(company);
        return getCompanyDto(company);
    }

    @NotNull
    private CompanyDto getCompanyDto(Company company) {
        CompanyDto companyDto1= companyMapper.toDto(company);
        companyDto1.setUser(userCompanyFeignClient.getUserById(company.getIdUser()).getBody());
        return companyDto1;
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream().map(this::getCompanyDto).toList();
    }

    @Override
    public CompanyDto getCompanyById(String id) {
        return getCompanyDto(getById(id));
    }

    @Override
    public CompanyDto getCompanyByName(String name) {
        return getCompanyDto(companyRepository.findByName(name).orElseThrow(()-> new CompanyNotFoundException(name)));
    }

    @Override
    public CompanyDto updateCompany(CompanyDto companyDto) {

        if(!companyRepository.findByName(companyDto.getName()).get().getId().equals(companyDto.getId())) {
            throw new CompanyAlreadyExistException(companyDto.getName());
        }
        Company company = getById(companyDto.getId());
        company.setName(companyDto.getName());
        company.setAddress(companyDto.getAddress());
        company.setCapital(companyDto.getCapital());
        company.setSector(companyDto.getSector());
        company.setEmail(companyDto.getEmail());
        company.setTel(companyDto.getTel());
        company.setCreationDate(companyDto.getCreationDate());

        return getCompanyDto(companyRepository.save(company));
    }

    @Override
    public String deleteCompany(String id) {
        companyRepository.delete(getById(id));
        return "company deleted";
    }

    private Company getById(String id) {
        return companyRepository.findById(id).orElseThrow(()-> new CompanyNotFoundException(id));
    }


}
