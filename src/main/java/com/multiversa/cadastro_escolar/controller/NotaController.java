package com.multiversa.cadastro_escolar.controller;
import com.multiversa.cadastro_escolar.dto.NotaDTO;
import com.multiversa.cadastro_escolar.model.Nota;
import com.multiversa.cadastro_escolar.repository.AlunoRepository;
import com.multiversa.cadastro_escolar.repository.DisciplinaRepository;
import com.multiversa.cadastro_escolar.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Autowired
    public NotaController(NotaRepository notaRepository, AlunoRepository alunoRepository, DisciplinaRepository disciplinaRepository) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @GetMapping
    public List<NotaDTO> getAllNotas() {
        List<Nota> notas = notaRepository.findAll();
        return notas.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneNota(@PathVariable(value = "id") Long id) {
        return notaRepository.findById(id)
                .map(this::convertToDto)
                .map(notaDTO -> ResponseEntity.status(HttpStatus.OK).body((Object) notaDTO))
                .orElseGet(() -> createNotFoundResponse(id));
    }

    @PostMapping
    public ResponseEntity<NotaDTO> createNota(@RequestBody NotaDTO notaDTO) {
        Nota nota = convertToEntity(notaDTO);
        Nota savedNota = notaRepository.save(nota);
        NotaDTO savedNotaDTO = convertToDto(savedNota);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateNota(@PathVariable Long id, @RequestBody NotaDTO notaDTO) {
        return updateOrDeleteNota(id, notaDTO, false);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNota(@PathVariable Long id) {
        return updateOrDeleteNota(id, null, true);
    }

    private ResponseEntity<Object> updateOrDeleteNota(Long id, NotaDTO notaDTO, boolean isDelete) {
        return notaRepository.findById(id)
                .map(existingNota -> {
                    if (isDelete) {
                        notaRepository.deleteById(id);
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                    } else {
                        Nota nota = convertToEntity(notaDTO);
                        nota.setId(id);
                        Nota updatedNota = notaRepository.save(nota);
                        NotaDTO updatedNotaDTO = convertToDto(updatedNota);
                        return ResponseEntity.status(HttpStatus.OK).body((Object) updatedNotaDTO);
                    }
                })
                .orElseGet(() -> createNotFoundResponse(id));
    }

    private ResponseEntity<Object> createNotFoundResponse(Long id) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("message", "Nota não encontrada.");
        errorResponse.put("details", "Nota com ID " + id + " não existe.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    private NotaDTO convertToDto(Nota nota) {
        return new NotaDTO(
                nota.getId(),
                nota.getAluno().getId(),
                nota.getDisciplina().getId(),
                nota.getValor(),
                nota.getDataAvaliacao()
        );
    }

    private Nota convertToEntity(NotaDTO notaDTO) {
        Nota nota = new Nota();
        nota.setId(notaDTO.getId());
        nota.setValor(notaDTO.getValor());
        nota.setDataAvaliacao(notaDTO.getDataAvaliacao());
        nota.setAluno(alunoRepository.findById(notaDTO.getAlunoId()).orElse(null));
        nota.setDisciplina(disciplinaRepository.findById(notaDTO.getDisciplinaId()).orElse(null));
        return nota;
    }
}
