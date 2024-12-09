package com.multiversa.cadastro_escolar.controller;
import com.multiversa.cadastro_escolar.dto.AlunoDTO;
import com.multiversa.cadastro_escolar.model.Aluno;
import com.multiversa.cadastro_escolar.repository.AlunoRepository;
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
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private static final String ENTITY_NAME = "Aluno";
    private static final String MESSAGE_KEY = "message";

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    @GetMapping
    public List<AlunoDTO> getAllAlunos() {
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.stream().map(this::convertToDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneAluno(@PathVariable(value = "id") Long id) {
        Optional<Aluno> alunoO = alunoRepository.findById(id);
        if (alunoO.isEmpty()) {
            return createNotFoundResponse(id);
        }
        Aluno aluno = alunoO.get();
        AlunoDTO alunoDTO = convertToDto(aluno);
        return ResponseEntity.status(HttpStatus.OK).body(alunoDTO);
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> createAluno(@RequestBody AlunoDTO alunoDTO) {
        Aluno aluno = convertToEntity(alunoDTO);
        Aluno savedAluno = alunoRepository.save(aluno);
        AlunoDTO savedAlunoDTO = convertToDto(savedAluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlunoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAluno(@PathVariable Long id, @RequestBody AlunoDTO alunoDTO) {
        if (!alunoRepository.existsById(id)) {
            return createNotFoundResponse(id);
        }
        Aluno aluno = convertToEntity(alunoDTO);
        aluno.setId(id);
        Aluno updatedAluno = alunoRepository.save(aluno);
        AlunoDTO updatedAlunoDTO = convertToDto(updatedAluno);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAlunoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAluno(@PathVariable Long id) {
        if (!alunoRepository.existsById(id)) {
            return createNotFoundResponse(id);
        }
        alunoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<Object> createNotFoundResponse(Long id) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put(MESSAGE_KEY, ENTITY_NAME + " não encontrado.");
        errorResponse.put("details", ENTITY_NAME + " com ID " + id + " não existe.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    private AlunoDTO convertToDto(Aluno aluno) {
        return new AlunoDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getDataNascimento(),
                aluno.getMatricula(),
                aluno.getTurma() != null ? aluno.getTurma().getId() : null
        );
    }

    private Aluno convertToEntity(AlunoDTO alunoDTO) {
        Aluno aluno = new Aluno();
        aluno.setId(alunoDTO.getId());
        aluno.setNome(alunoDTO.getNome());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setMatricula(alunoDTO.getMatricula());
        aluno.setDataNascimento(alunoDTO.getDataNascimento());
        aluno.setTurma(turmaRepository.findById(alunoDTO.getTurmaId()).orElse(null));
        return aluno;
    }
}
