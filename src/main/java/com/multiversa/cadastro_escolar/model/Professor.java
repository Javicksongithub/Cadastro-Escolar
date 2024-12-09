package com.multiversa.cadastro_escolar.model;

import javax.persistence.*; // Import correto para javax.persistence
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String disciplinaPrincipal;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Disciplina> disciplinas; // Um professor ensina várias disciplinas.

    @ManyToMany(mappedBy = "professores")
    private List<Turma> turmas; // Um professor pode estar associado a várias turmas.
}
