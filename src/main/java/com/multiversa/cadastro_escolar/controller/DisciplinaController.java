package com.multiversa.cadastro_escolar.controller;
import com.multiversa.cadastro_escolar.dto.DisciplinaDTO;
import com.multiversa.cadastro_escolar.model.Disciplina;
import com.multiversa.cadastro_escolar.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private static final String ENTITY_NAME = "Disciplina";
    private static final String MESSAGE_KEY = "message";

    private final DisciplinaRepository disciplinaRepository;

    @Autowired
    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping
    public List<DisciplinaDTO> getAllDisciplinas() {
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        return disciplinas.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneDisciplina(@PathVariable(value = "id") Long id) {
        Optional<Disciplina> disciplinaO = disciplinaRepository.findById(id);
        if (disciplinaO.isEmpty()) {
            return createNotFoundResponse(id);
        }
        Disciplina disciplina = disciplinaO.get();
        DisciplinaDTO disciplinaDTO = convertToDto(disciplina);
        return ResponseEntity.status(HttpStatus.OK).body(disciplinaDTO);
    }

    @PostMapping
    public ResponseEntity<DisciplinaDTO> createDisciplina(@RequestBody DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = convertToEntity(disciplinaDTO);
        Disciplina savedDisciplina = disciplinaRepository.save(disciplina);
        DisciplinaDTO savedDisciplinaDTO = convertToDto(savedDisciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDisciplinaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDisciplina(@PathVariable Long id, @RequestBody DisciplinaDTO disciplinaDTO) {
        if (!disciplinaRepository.existsById(id)) {
            return createNotFoundResponse(id);
        }
        Disciplina disciplina = convertToEntity(disciplinaDTO);
        disciplina.setId(id);
        Disciplina updatedDisciplina = disciplinaRepository.save(disciplina);
        DisciplinaDTO updatedDisciplinaDTO = convertToDto(updatedDisciplina);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDisciplinaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDisciplina(@PathVariable Long id) {
        if (!disciplinaRepository.existsById(id)) {
            return createNotFoundResponse(id);
        }
        disciplinaRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<Object> createNotFoundResponse(Long id) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put(MESSAGE_KEY, ENTITY_NAME + " não encontrada.");
        errorResponse.put("details", ENTITY_NAME + " com ID " + id + " não existe.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    private DisciplinaDTO convertToDto(Disciplina disciplina) {
        return new DisciplinaDTO(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getCargaHoraria(),
                disciplina.getProfessor().getId()
        );
    }

    private Disciplina convertToEntity(DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaDTO.getId());
        disciplina.setNome(disciplinaDTO.getNome());
        disciplina.setCargaHoraria(disciplinaDTO.getCargaHoraria());
        // Aqui você precisaria buscar e definir o Professor baseado no professorId
        // disciplina.setProfessor(professorRepository.findById(disciplinaDTO.getProfessorId()).orElse(null));
        return disciplina;
    }
}
