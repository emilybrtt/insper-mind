package br.insper.insperMind.common;

import br.insper.insperMind.material.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${file.upload-dir:uploads/materiais}")
    private String uploadDir;
    private static final long MAX_SIZE = 100 * 1024 * 1024L;
    private static final List<String> ALLOWED_TYPES = List.of(
            "application/pdf","application/msword",
            "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public String salvarArquivo(MultipartFile file) {
        if (file.getSize() > MAX_SIZE) throw new InvalidFileException();
        if (!ALLOWED_TYPES.contains(file.getContentType())) throw new InvalidFileException();
        try {
            byte[] bytes = file.getBytes();
            if (!isValidMagicBytes(bytes)) throw new InvalidFileException();
            Files.createDirectories(Paths.get(uploadDir));
            String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(uploadDir, nomeArquivo);
            Files.write(caminho, bytes);
            return nomeArquivo;
        } catch (IOException e) {
            throw new FileSaveException();
        }
    }

    private boolean isValidMagicBytes(byte[] b) {
        if (b.length < 4) return false;
        boolean isPdf   = b[0] == 0x25 && b[1] == 0x50;
        boolean isOle2  = (b[0] & 0xFF) == 0xD0 && (b[1] & 0xFF) == 0xCF;
        boolean isOoxml = b[0] == 0x50 && b[1] == 0x4B;
        return isPdf || isOle2 || isOoxml;
    }
}