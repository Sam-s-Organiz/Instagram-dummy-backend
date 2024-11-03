package com.Instagram.Dummy.validations;

import java.util.List;

public class FileUploadValidator {

    public String validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return "Image URL is required";
        }
        List<String> allowedExtensions = List.of("jpg", "jpeg", "png", "gif");
        String fileExtension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
        if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
            return "Invalid image type. Allowed types are: " + String.join(", ", allowedExtensions);
        }
        return null;
    }
}
