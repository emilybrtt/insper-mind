package br.insper.insperMind.common;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final String uploadDir = "uploads/materiais";

    public String salvarArquivo(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(uploadDir, nomeArquivo);
            Files.write(caminho, file.getBytes());
            return nomeArquivo;
        } catch (IOException e) {
            throw new FileSaveException();
        }
    }
}