package com.multiversa.cadastro_escolar.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {
    private Long id;
    private String email;
    private String disciplinaPrincipal;
    private List<Long> disciplinaIds; // IDs das disciplinas para evitar loops infinitos.
    private List<Long> turmaIds; // IDs das turmas para evitar loops infinitos.
}
