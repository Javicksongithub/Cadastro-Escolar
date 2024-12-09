package com.multiversa.cadastro_escolar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String matricula;
    private Long turmaId;
}
