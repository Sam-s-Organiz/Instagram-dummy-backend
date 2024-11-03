package com.Instagram.Dummy.adapter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;

public class FilePartAdapter implements FilePart {
    private final MultipartFile multipartFile;

    public FilePartAdapter(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    @Override
    public String filename() {
        return multipartFile.getOriginalFilename();
    }

    @Override
    public Mono<Void> transferTo(Path destination) {
        return Mono.fromRunnable(() -> {
            try {
                multipartFile.transferTo(destination.toFile());
            } catch (IOException e) {
                throw new RuntimeException("File transfer failed", e);
            }
        });
    }

    @Override
    public String name() {
        return multipartFile.getName(); // Return the name of the multipart file
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public Flux<DataBuffer> content() {
        return null;
    }

    // Implement other FilePart methods as needed...
}
