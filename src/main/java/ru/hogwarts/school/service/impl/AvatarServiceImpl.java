package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
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
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        createDirectory();

        Path filePath = Path.of(pathDir, UUID.randomUUID() + "." + getExtension(multipartFile.getOriginalFilename()));

        avatarRepository.save(new Avatar(
                filePath.toString(),
                multipartFile.getSize(),
                multipartFile.getContentType(),
                multipartFile.getBytes(),
                student
        ));

        multipartFile.transferTo(filePath);

//        try(InputStream is = multipartFile.getInputStream();
//            OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
//            BufferedInputStream bis = new BufferedInputStream(is, 1024);
//            BufferedOutputStream bos = new BufferedOutputStream(os, 1024)){
//            bis.transferTo(bos);
//        }
//        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath))){
//            bufferedOutputStream.write(multipartFile.getBytes(), 0, multipartFile.getBytes().length);
//        }

//        Files.copy(multipartFile.getInputStream(), filePath);
//        multipartFile.transferTo(filePath);

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
