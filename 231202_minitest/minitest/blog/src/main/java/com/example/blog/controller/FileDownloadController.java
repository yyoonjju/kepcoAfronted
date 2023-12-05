package com.example.blog.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileDownloadController {
     
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        // 파일 경로 설정 (이 부분을 실제 파일이 저장된 경로로 수정해야 합니다)
        Path filePath = Paths.get("c:\\uploadfile\\directory\\" + fileName);

        // 파일을 읽어올 Resource 객체 생성
        Resource resource = new org.springframework.core.io.PathResource(filePath);

        // Content-Disposition 헤더를 설정하여 브라우저가 다운로드 창을 띄울 수 있게 함
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}