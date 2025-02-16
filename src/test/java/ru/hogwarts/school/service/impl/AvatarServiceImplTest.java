package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "application.properties", properties = "path.dir=image")
class AvatarServiceImplTest {
    @InjectMocks
    private AvatarServiceImpl avatarService;

    @Mock
    private  StudentRepository studentRepository;

    @Mock
    private  AvatarRepository avatarRepository;



    @Test
    void uploadImage() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn(new byte[1024]);
        when(multipartFile.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
        when(multipartFile.getOriginalFilename()).thenReturn("image.jpg");
        when(multipartFile.getSize()).thenReturn(1024L);


        Student student = new Student("Harry", 18);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(avatarRepository.save(any(Avatar.class))).thenReturn(null);

        doNothing().when(multipartFile).transferTo(any(Path.class));

        //test
        avatarService.uploadImage(nextLong(), multipartFile);
    }

    @Test
    void getAvatarFromDB() {
    }

    @Test
    void getAvatarFromLocal() {
    }
}

//@Override
//public void uploadImage(long studentId, MultipartFile multipartFile) throws IOException {
//
//
//    createDirectory();
//
//    Path filePath = Path.of(pathDir, UUID.randomUUID() + "." + getExtension(multipartFile.getOriginalFilename()));
//
//    createAvatar(studentId, multipartFile, filePath.toString());
//
//    multipartFile.transferTo(filePath);
//
//}