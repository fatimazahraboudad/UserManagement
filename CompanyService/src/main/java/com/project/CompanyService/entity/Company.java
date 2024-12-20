package com.project.CompanyService.entity;

import com.project.CompanyService.enumeration.CompanyType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Company {

    @Id
    private String id;
    private String name;
    private String sector;
    private String address;
    private String tel;
    private String email;
    private LocalDate creationDate;
    private String capital;
    private CompanyType companyType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private String idUser ;

}
