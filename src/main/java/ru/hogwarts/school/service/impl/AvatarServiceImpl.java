package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

@Service
public class AvatarServiceImpl implements AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private String pathDir;

    private final StudentRepository studentRepository;

    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl( @Value("${path.dir}") String pathDir, StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.pathDir = pathDir;
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public void uploadImage(long studentId, MultipartFile multipartFile) throws IOException {
        logger.info("Was invoked method to upload avatar for student with id = {}", studentId);

        System.out.println(pathDir);

        createDirectory();

        Path filePath = Path.of(pathDir, UUID.randomUUID() + "." + getExtension(multipartFile.getOriginalFilename()));

        createAvatar(studentId, multipartFile, filePath.toString());

        multipartFile.transferTo(filePath);
        logger.debug("Image saved to file system at {}", filePath);

    }

    @Override
    public Avatar getAvatarFromDB(long studentId) {
        logger.info("Getting avatar from DB for student with id = {}", studentId);

        if (!studentRepository.existsById(studentId)) {
            logger.error("Student with id {} not found", studentId);
            throw new StudentNotFoundException(studentId);
        }

        return avatarRepository.getByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Avatar not found for student id = {}", studentId);
                    return new AvatarNotFoundException();
                });
    }

    @Override
    public byte[] getAvatarFromLocal(long studentId) {
        logger.info("Getting avatar from local storage for student with id = {}", studentId);

        if (!studentRepository.existsById(studentId)) {
            logger.error("Student with id {} not found", studentId);
            throw new StudentNotFoundException(studentId);
        }

        Avatar avatar = avatarRepository.getByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Avatar not found for student id = {}", studentId);
                    return new AvatarNotFoundException();
                });

        String filePath = avatar.getFilePath();

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath))) {
            return bufferedInputStream.readAllBytes();
        } catch (IOException e) {
            logger.error("Error reading image from disk: {}", e.getMessage());
            throw new IllegalArgumentException("Чтение картинки не удалось: " + e.getMessage());
        }
    }

    private void createAvatar(long studentId, MultipartFile multipartFile, String filePath) throws IOException {
        logger.debug("Creating avatar entity for student id = {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student not found while creating avatar, id = {}", studentId);
                    return new StudentNotFoundException(studentId);
                });

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
            logger.warn("Directory {} does not exist. Creating...", pathDir);
            Files.createDirectory(path);
        }
    }

    @Override
    public Page<Avatar> getAvatars(Pageable pageable) {
        logger.debug("Getting paginated avatars list");

        return avatarRepository.findAll(pageable);
    }


}

