package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarServiceImpl {
    @Value("${path.dir}")
    private String pathDir;

    private final StudentRepository studentRepository;

    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public void uploadImage(long studentId, MultipartFile multipartFile) throws IOException {


        createDirectory();

        Path filePath = Path.of(pathDir, UUID.randomUUID() + "." + getExtension(multipartFile.getOriginalFilename()));

        createAvatar(studentId, multipartFile, filePath.toString());

        multipartFile.transferTo(filePath);

    }

    public Avatar getAvatarFromDB(long studentId) {
        boolean studentExist = studentRepository.existsById(studentId);
        if (!studentExist) {
            throw new StudentNotFoundException(studentId);
        }


        return avatarRepository.getByStudentId(studentId)
               .orElseThrow(AvatarNotFoundException::new);
    }

    public byte[] getAvatarFromLocal(long studentId) {
        boolean studentExist = studentRepository.existsById(studentId);
        if (!studentExist) {
            throw new StudentNotFoundException(studentId);
        }

        Avatar avatar = avatarRepository.getByStudentId(studentId)
                .orElseThrow(AvatarNotFoundException::new);
        String filePath = avatar.getFilePath();
        try (BufferedInputStream bufferedOutputStream = new BufferedInputStream(new FileInputStream(filePath))) {
            return bufferedOutputStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Чтение картинки не удалось: " + e.getMessage());
        }
    }

    private void createAvatar(long studentId, MultipartFile multipartFile, String filePath) throws IOException{
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        avatarRepository.save(new Avatar(
                filePath,
                multipartFile.getSize(),
                multipartFile.getContentType(),
                multipartFile.getBytes(),
                student
        ));
    }


    private String getExtension(String originalPath) {
        return originalPath.substring(originalPath.lastIndexOf(".") + 1);
    }

    private void createDirectory() throws IOException {
        Path path = Path.of(pathDir);
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
    }

}
