package com.hengthay.ecommercebackendspring.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;
    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp", "image/jpg");

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    // Create method to store file
    public String storeImageFile(MultipartFile file, String subFolder) {
        // if file empty
        if(file == null || file.isEmpty()) {
            return null;
        }
        // get contentType like image/jpg, image/jpeg
        String contentType = file.getContentType();

        // check if content type null or content not allow to upload
        if(contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only accept JPG, JPEG, PNG and WebP images.");
        }

        try {
            // to append new sub folder like uploads/products
            Path targetDir = this.rootLocation.resolve(subFolder).normalize();
            // create directory
            Files.createDirectories(targetDir);

            // get file original name extension
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            // generate unique name
            String uniqueFilename = UUID.randomUUID() + (extension != null ? "." + extension : "");

            // to append new file
            Path targetFile = targetDir.resolve(uniqueFilename);
            // to copy the URL path like /uploads/product/uuid.jpg
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            // return as format /uploads/products/uuid.jpg
            return "/uploads/" + subFolder + "/" + uniqueFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file in " + subFolder, ex);
        }
    }

    // Create method to delete file
    public void deleteFile(String fileUrl) {
        // check if file empty
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            // Convert "/uploads/products/abc.jpg" -> "products/abc.jpg"
            String relativePath = fileUrl.replaceFirst("^/uploads/", "");
            // Get the file path
            Path filePath = this.rootLocation.resolve(relativePath).normalize();
            // Remove that file
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete file: " + fileUrl, ex);
        }
    }
}
