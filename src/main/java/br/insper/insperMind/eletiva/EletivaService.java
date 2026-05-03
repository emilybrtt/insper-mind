package br.insper.insperMind.eletiva;

import br.insper.insperMind.eletiva.dto.EditEletivaDTO;
import br.insper.insperMind.eletiva.dto.ResponseEletivaDTO;
import br.insper.insperMind.eletiva.dto.SaveEletivaDTO;
import br.insper.insperMind.eletiva.exception.EletivaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EletivaService {

    @Autowired
    private EletivaRepository eletivaRepository;

    public ResponseEletivaDTO save(SaveEletivaDTO saveEletivaDTO) {
        Eletiva eletiva = Eletiva.toModel(saveEletivaDTO);
        eletiva = eletivaRepository.save(eletiva);

        return ResponseEletivaDTO.toDTO(eletiva);
    }

    public Page<ResponseEletivaDTO> list(Pageable pageable) {
        return eletivaRepository
                .findByAtivoTrue(pageable)
                .map(eletiva -> ResponseEletivaDTO.toDTO(eletiva));
    }

    public Eletiva get(Integer id) {
        Eletiva eletiva = eletivaRepository.findById(id)
                .orElseThrow(() -> new EletivaNotFoundException("Eletiva não encontrada"));

        if (!eletiva.getAtivo()) {
            throw new RuntimeException("Não encontrado");
        }

        return eletiva;
    }

    public Eletiva findEntityById(Integer id) {
        return eletivaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Eletiva não encontrada"));
    }


    public ResponseEletivaDTO getDTO(Integer id) {
        return ResponseEletivaDTO.toDTO(get(id));
    }

    public ResponseEletivaDTO edit(Integer id, EditEletivaDTO editEletivaDTO) {
        Eletiva eletivaDB = get(id);

        eletivaDB.setCargaHoraria(editEletivaDTO.getCargaHoraria());
        eletivaDB.setSemestreMinimo(editEletivaDTO.getSemestreMinimo());

        eletivaDB = eletivaRepository.save(eletivaDB);
        return ResponseEletivaDTO.toDTO(eletivaDB);
    }

    public void delete(Integer id) {
        Eletiva eletiva = get(id);
        eletiva.setAtivo(false);
        eletivaRepository.save(eletiva);
        }
    }
