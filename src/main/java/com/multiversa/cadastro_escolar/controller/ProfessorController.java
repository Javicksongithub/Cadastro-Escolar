package com.multiversa.cadastro_escolar.controller;
import com.multiversa.cadastro_escolar.dto.ProfessorDTO;
import com.multiversa.cadastro_escolar.model.Disciplina;
import com.multiversa.cadastro_escolar.model.Turma;
import com.multiversa.cadastro_escolar.model.Professor;
import com.multiversa.cadastro_escolar.repository.ProfessorRepository;
import com.multiversa.cadastro_escolar.repository.DisciplinaRepository;
import com.multiversa.cadastro_escolar.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private static final String ENTITY_NAME = "Professor";
    private static final String MESSAGE_KEY = "message";

    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final TurmaRepository turmaRepository;

    @Autowired
    public ProfessorController(ProfessorRepository professorRepository, DisciplinaRepository disciplinaRepository, TurmaRepository turmaRepository) {
        this.professorRepository = professorRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.turmaRepository = turmaRepository;
    }

    @GetMapping
    public List<ProfessorDTO> getAllProfessores() {
        List<Professor> professores = professorRepository.findAll();
        return professores.stream().map(this::convertToDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProfessor(@PathVariable(value = "id") Long id) {
        return professorRepository.findById(id)
                .map(this::convertToDto)
                .map(professorDTO -> ResponseEntity.status(HttpStatus.OK).body((Object) professorDTO))
                .orElseGet(() -> createNotFoundResponse(id));
    }

    @PostMapping
    public ResponseEntity<ProfessorDTO> createProfessor(@RequestBody ProfessorDTO professorDTO) {
        Professor professor = convertToEntity(professorDTO);
        Professor savedProfessor = professorRepository.save(professor);
        ProfessorDTO savedProfessorDTO = convertToDto(savedProfessor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfessorDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProfessor(@PathVariable Long id, @RequestBody ProfessorDTO professorDTO) {
        return updateOrDeleteProfessor(id, professorDTO, false);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProfessor(@PathVariable Long id) {
        return updateOrDeleteProfessor(id, null, true);
    }

    private ResponseEntity<Object> updateOrDeleteProfessor(Long id, ProfessorDTO professorDTO, boolean isDelete) {
        return professorRepository.findById(id)
                .map(existingProfessor -> {
                    if (isDelete) {
                        professorRepository.deleteById(id);
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                    } else {
                        Professor professor = convertToEntity(professorDTO);
                        professor.setId(id);
                        Professor updatedProfessor = professorRepository.save(professor);
                        ProfessorDTO updatedProfessorDTO = convertToDto(updatedProfessor);
                        return ResponseEntity.status(HttpStatus.OK).body((Object) updatedProfessorDTO);
                    }
                })
                .orElseGet(() -> createNotFoundResponse(id));
    }

    private ResponseEntity<Object> createNotFoundResponse(Long id) {
        Map<String, String> errorResponse = createErrorResponse(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    private Map<String, String> createErrorResponse(Long id) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put(MESSAGE_KEY, ENTITY_NAME + " não encontrado.");
        errorResponse.put("details", ENTITY_NAME + " com ID " + id + " não existe.");
        return errorResponse;
    }

    private ProfessorDTO convertToDto(Professor professor) {
        List<Long> disciplinaIds = professor.getDisciplinas().stream()
                .map(Disciplina::getId)
                .toList();
        List<Long> turmaIds = professor.getTurmas().stream()
                .map(Turma::getId)
                .toList();
        return new ProfessorDTO(
                professor.getId(),
                professor.getEmail(),
                professor.getDisciplinaPrincipal(),
                disciplinaIds,
                turmaIds
        );
    }

    private Professor convertToEntity(ProfessorDTO professorDTO) {
        Professor professor = new Professor();
        professor.setId(professorDTO.getId());
        professor.setEmail(professorDTO.getEmail());
        professor.setDisciplinaPrincipal(professorDTO.getDisciplinaPrincipal());
        professor.setDisciplinas(professorDTO.getDisciplinaIds().stream()
                .map(disciplinaRepository::findById)
                .map(Optional::orElseThrow)
                .toList());
        professor.setTurmas(professorDTO.getTurmaIds().stream()
                .map(turmaRepository::findById)
                .map(Optional::orElseThrow)
                .toList());
        return professor;
    }
}
