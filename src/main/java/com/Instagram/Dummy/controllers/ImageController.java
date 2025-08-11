package com.Instagram.Dummy.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
public class ImageController {

  private static final String BASE_UPLOAD_DIR = "uploads/posts/";

  @GetMapping("/user_{userId}/{filename:.+}")
  public ResponseEntity<Resource> getImage(
      @PathVariable Long userId, @PathVariable String filename) {

    try {
      Path filePath =
          Paths.get(BASE_UPLOAD_DIR).resolve("user_" + userId).resolve(filename).normalize();

      Resource resource = new UrlResource(filePath.toUri());

      if (!resource.exists()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

      String contentType = Files.probeContentType(filePath);
      if (contentType == null) {
        contentType = "application/octet-stream"; // fallback
      }

      return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

    } catch (MalformedURLException e) {
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
