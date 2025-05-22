package com.Instagram.Dummy.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {
  public static byte[] resizeImage(MultipartFile file, int width, int height) throws IOException {
    BufferedImage original = ImageIO.read(file.getInputStream());
    Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = resized.createGraphics();
    g2d.drawImage(scaled, 0, 0, null);
    g2d.dispose();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(resized, "JPEG", outputStream);
    return outputStream.toByteArray();
  }

  public static String saveImageToLocal(MultipartFile file, Long userId) throws IOException {
    String folderPath = "uploads/posts/user_" + userId;
    File dir = new File(folderPath);
    if (!dir.exists()) dir.mkdirs();

    String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path path = Paths.get(folderPath, filename);
    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

    // Return relative path for serving via Spring
    return "/images/user_" + userId + "/" + filename;
  }
}
