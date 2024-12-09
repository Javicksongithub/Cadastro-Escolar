package com.multiversa.cadastro_escolar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.multiversa.cadastro_escolar.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    // Declarações de métodos personalizados, se necessário
}
