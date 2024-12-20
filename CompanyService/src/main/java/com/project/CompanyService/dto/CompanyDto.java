package com.project.CompanyService.dto;

import com.project.CompanyService.enumeration.CompanyType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CompanyDto {

    private String id;
    private String name;
    private String sector;
    private String address;
    private String tel;
    private String email;
    private LocalDate creationDate;
    private String capital;
    private CompanyType companyType;
    private UserDto user;
}
