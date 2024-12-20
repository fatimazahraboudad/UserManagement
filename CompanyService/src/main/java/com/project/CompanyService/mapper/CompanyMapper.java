package com.project.CompanyService.mapper;

import com.project.CompanyService.dto.CompanyDto;
import com.project.CompanyService.entity.Company;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company toEntity(CompanyDto companyDto);
    CompanyDto toDto(Company company);
    List<CompanyDto> toDto(List<Company> companyList);

}
