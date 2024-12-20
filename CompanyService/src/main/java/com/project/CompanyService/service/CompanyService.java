package com.project.CompanyService.service;

import com.project.CompanyService.dto.CompanyDto;

import java.util.List;

public interface CompanyService {


    CompanyDto addCompany(CompanyDto companyDto);
    List<CompanyDto> getAllCompanies();
    CompanyDto getCompanyById(String id);
    CompanyDto getCompanyByName(String name);
    CompanyDto updateCompany(CompanyDto companyDto);
    String deleteCompany(String id);
}
