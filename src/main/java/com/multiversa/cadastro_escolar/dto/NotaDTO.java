package com.multiversa.cadastro_escolar.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTO {
    private Long id;
    private Long alunoId;
    private Long disciplinaId;
    private double valor;
    private LocalDate dataAvaliacao;
}
