package br.insper.insperMind.common;

import br.insper.insperMind.material.exception.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {
    private final String uploadDir = "uploads/materiais";
    private static final long MAX_SIZE = 100 * 1024 * 1024L;
    private static final List<String> ALLOWED_TYPES = List.of(
            "application/pdf","application/msword",
            "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public String salvarArquivo(MultipartFile file) {
        if (file.getSize() > MAX_SIZE) throw new InvalidFileException();
        if (!ALLOWED_TYPES.contains(file.getContentType())) throw new InvalidFileException();
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