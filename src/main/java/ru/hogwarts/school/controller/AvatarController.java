package ru.hogwarts.school.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.impl.AvatarServiceImpl;

import java.awt.*;
import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarServiceImpl avatarService;

    public AvatarController(AvatarServiceImpl avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(path = "/upload/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImage(@PathVariable("studentId") long studentId,
                            @RequestBody MultipartFile multipartFile) throws IOException {
        avatarService.uploadImage(studentId, multipartFile);
    }
}
