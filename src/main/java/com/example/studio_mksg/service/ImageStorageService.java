package com.example.studio_mksg.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final String UPLOAD_DIR = "src/main/resources/static/itemImage/";

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        String original = file.getOriginalFilename();
        String ext = "";

        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.') + 1);
        }
        // ファイル名重複防止
        String fileName = UUID.randomUUID() + "." + ext;

        Path savePath = Paths.get(UPLOAD_DIR, fileName);
        Files.createDirectories(savePath.getParent());

        try {
            Files.copy(file.getInputStream(), savePath);
        } catch (IOException e) {
            throw new RuntimeException("画像保存に失敗しました", e);
        }
        // DBに保存するパス
        return "/itemImage/" + fileName;
    }
}
