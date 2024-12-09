package com.multiversa.cadastro_escolar.repository;

import com.multiversa.cadastro_escolar.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {}
