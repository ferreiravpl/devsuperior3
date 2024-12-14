package com.javaspringprofessional.desafio3.dto;

import com.javaspringprofessional.desafio3.entities.Client;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

import static com.javaspringprofessional.desafio3.util.Constants.NOT_ALLOWED_FUTURE_DATE;
import static com.javaspringprofessional.desafio3.util.Constants.REQUIRED_FIELD;

public class ClientDTO {

    private Long id;

    @NotBlank(message = REQUIRED_FIELD)
    private String name;
    private String cpf;
    private Double income;

    @PastOrPresent(message = NOT_ALLOWED_FUTURE_DATE)
    private LocalDate birthDate;
    private Integer children;

    public ClientDTO() {

    }

    public ClientDTO(Client entity) {
        id = entity.getId();
        name = entity.getName();
        cpf = entity.getCpf();
        income = entity.getIncome();
        birthDate = entity.getBirthDate();
        children = entity.getChildren();
    }

    public ClientDTO(Long id, String name, String cpf, Double income, LocalDate birthDate, Integer children) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.income = income;
        this.birthDate = birthDate;
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Double getIncome() {
        return income;
    }

    public Integer getChildren() {
        return children;
    }
}
