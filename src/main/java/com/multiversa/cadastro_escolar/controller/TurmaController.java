package com.multiversa.cadastro_escolar.controller;

import com.multiversa.cadastro_escolar.dto.TurmaDTO;
import com.multiversa.cadastro_escolar.model.Turma;
import com.multiversa.cadastro_escolar.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/turmas")
@RequiredArgsConstructor
public class TurmaController {

    private static final String ENTITY_NOT_FOUND_MESSAGE = "Turma não encontrada com o ID ";
    private static final String MESSAGE_KEY = "message";
    private static final String TIMESTAMP_KEY = "timestamp";

    private final TurmaRepository turmaRepository;

    @GetMapping
    public ResponseEntity<List<TurmaDTO>> getAllTurmas() {
        List<Turma> turmas = turmaRepository.findAll();
        List<TurmaDTO> turmasDTO = turmas.stream()
                .map(TurmaDTO::new)
                .toList();
        return ResponseEntity.ok(turmasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTurmaById(@PathVariable Long id) {
        Optional<Turma> turma = turmaRepository.findById(id);
        if (turma.isPresent()) {
            return ResponseEntity.ok(new TurmaDTO(turma.get()));
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
            errorResponse.put(MESSAGE_KEY, ENTITY_NOT_FOUND_MESSAGE + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createTurma(@RequestBody TurmaDTO turmaDTO) {
        try {
            Turma turma = turmaDTO.toEntity();
            Turma savedTurma = turmaRepository.save(turma);
            return ResponseEntity.status(HttpStatus.CREATED).body(new TurmaDTO(savedTurma));
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
            errorResponse.put(MESSAGE_KEY, "Erro ao criar a turma: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTurma(@PathVariable Long id, @RequestBody TurmaDTO turmaDTO) {
        Optional<Turma> existingTurma = turmaRepository.findById(id);
        if (existingTurma.isPresent()) {
            Turma turma = turmaDTO.toEntity();
            turma.setId(id);
            Turma updatedTurma = turmaRepository.save(turma);
            return ResponseEntity.ok(new TurmaDTO(updatedTurma));
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
            errorResponse.put(MESSAGE_KEY, ENTITY_NOT_FOUND_MESSAGE + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTurma(@PathVariable Long id) {
        Optional<Turma> turma = turmaRepository.findById(id);
        if (turma.isPresent()) {
            turmaRepository.delete(turma.get());
            return ResponseEntity.noContent().build();
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
            errorResponse.put(MESSAGE_KEY, ENTITY_NOT_FOUND_MESSAGE + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
