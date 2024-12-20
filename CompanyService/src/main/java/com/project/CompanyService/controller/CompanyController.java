package com.project.CompanyService.controller;

import com.project.CompanyService.dto.CompanyDto;
import com.project.CompanyService.entity.Company;
import com.project.CompanyService.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/add")
    public ResponseEntity<CompanyDto> add(@RequestBody CompanyDto companyDto) {
        return new ResponseEntity<>(companyService.addCompany(companyDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CompanyDto>> all() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable String id) {
        return new ResponseEntity<>(companyService.getCompanyById(id), HttpStatus.OK);
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<CompanyDto> getByName(@PathVariable String name) {
        return new ResponseEntity<>(companyService.getCompanyByName(name), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<CompanyDto> update(@RequestBody CompanyDto companyDto) {
        return new ResponseEntity<>(companyService.updateCompany(companyDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return new ResponseEntity<>(companyService.deleteCompany(id), HttpStatus.OK);
    }
}
