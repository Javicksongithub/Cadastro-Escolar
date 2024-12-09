package com.multiversa.cadastro_escolar.model;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disciplina {
    // Método getId explicitamente adicionado
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int cargaHoraria;

    @ManyToOne
    private Professor professor; // Uma disciplina é ensinada por um professor.

    @ManyToMany(mappedBy = "disciplinas")
    private List<Turma> turmas; // Uma disciplina é oferecida em várias turmas.

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private List<Nota> notas; // Uma disciplina pode ter várias notas relacionadas.

}
