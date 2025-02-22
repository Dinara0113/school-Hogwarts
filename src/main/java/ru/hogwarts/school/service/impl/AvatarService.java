package ru.hogwarts.school.service.impl;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;

public interface AvatarService {
    void uploadImage(long studentId, MultipartFile multipartFile) throws IOException;

    Avatar getAvatarFromDB(long studentId);

    byte[] getAvatarFromLocal(long studentId);
}
