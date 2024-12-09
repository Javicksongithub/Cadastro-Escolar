package com.multiversa.cadastro_escolar.dto;
import com.multiversa.cadastro_escolar.model.Turma;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurmaDTO {
    private Long id;
    private String nome;
    private int ano;
    private List<Long> alunoIds; // IDs dos alunos para evitar loops infinitos.
    private List<Long> disciplinaIds; // IDs das disciplinas para evitar loops infinitos.
    private List<Long> professorIds; // IDs dos professores para evitar loops infinitos.

    public TurmaDTO(Turma turma) {
    }

    public Turma toEntity() {

        return null;
    }}
