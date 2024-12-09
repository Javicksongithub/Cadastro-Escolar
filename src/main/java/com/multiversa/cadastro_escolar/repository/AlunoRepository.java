package com.multiversa.cadastro_escolar.repository;


import com.multiversa.cadastro_escolar.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

}