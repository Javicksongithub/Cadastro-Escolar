package com.multiversa.cadastro_escolar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaDTO {
    private Long id;
    private String nome;
    private int cargaHoraria;
    private Long professorId; // Usar o ID do professor para evitar loops infinitos.
}

