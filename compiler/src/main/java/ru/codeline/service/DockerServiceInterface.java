package ru.codeline.service;

import org.springframework.web.multipart.MultipartFile;
import ru.codeline.model.Result;

import java.io.IOException;

public interface DockerServiceInterface {
    int buildImage(String folder, String imageName);
    Result executeCode(String imageName, MultipartFile outputFile);
    String getRunningContainers() throws IOException;
    String getRunningImages() throws IOException;

    String deleteImage(String imageName) throws IOException;
}
