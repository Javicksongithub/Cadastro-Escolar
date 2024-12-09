package com.multiversa.cadastro_escolar.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Aluno aluno; // A nota está associada a um aluno.

    @ManyToOne
    private Disciplina disciplina; // A nota está associada a uma disciplina.

    private double valor;
    private LocalDate dataAvaliacao;
}
