package ru.webency.compiler.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.webency.compiler.exceptions.CompilerException;
import ru.webency.compiler.exceptions.DockerException;
import ru.webency.compiler.model.Language;
import ru.webency.compiler.model.Request;
import ru.webency.compiler.model.Response;
import ru.webency.compiler.model.Result;
import ru.webency.compiler.utils.EntryPointFile;
import ru.webency.compiler.utils.FileHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;



@Slf4j
@Service
@AllArgsConstructor
public class CompilerService implements CompilerServiceInterface {
    private DockerServiceInterface dockerService;

    @Override
    public ResponseEntity<Object> compile(Request request) throws CompilerException, IOException {
        return compile(request.getSourceCode(), request.getInput(), request.getExpectedOutput(),
                request.getLanguage(), request.getTimeLimit(), request.getMemoryLimit());
    }

    @Override
    public ResponseEntity<Object> compile(
            MultipartFile sourceCode,
            MultipartFile inputFile,
            MultipartFile outputFile,
            Language language,
            int timeLimit, int memoryLimit) throws CompilerException {

        String imageName = UUID.randomUUID().toString();

        if (memoryLimit < 0 || memoryLimit > 1000)
            return ResponseEntity
                    .badRequest()
                    .body("Error memoryLimit must be between 0Mb and 1000Mb");

        if (timeLimit < 0 || timeLimit > 15)
            return ResponseEntity
                    .badRequest()
                    .body("Error timeLimit must be between 0 Sec and 15 Sec");


        String folder = language.getFolder();
        String file = language.getFile();

        LocalDateTime date = LocalDateTime.now();

        synchronized (this) {
            createEntryPoint(sourceCode, inputFile, language, timeLimit, memoryLimit);
            log.info("{}|| entryPoint file has been created", imageName);

            try {
                // Save the uploaded files
                FileHandler.saveUploadedFile(sourceCode, folder + "/" + file);
                FileHandler.saveUploadedFile(outputFile, folder + "/" + outputFile.getOriginalFilename());
                if (inputFile != null) {
                    FileHandler.saveUploadedFile(inputFile, folder + "/" + inputFile.getOriginalFilename());
                }
                log.info("{}|| Files have been saved", imageName);

                // Build the Docker image
                log.info("{}|| Start building image", imageName);
                AtomicInteger status = new AtomicInteger(dockerService.buildImage(folder, imageName));
                if (status.get() == 0) {
                    log.info("{}|| Image has been built", imageName);
                } else {
                    throw new DockerException(imageName + "|| Error while building image");
                }

                // Execute the code in the Docker container
                Result result = dockerService.executeCode(imageName, outputFile);
                String statusResponse = result.getStatus();
                log.info("Status response is {}", statusResponse);

                // Return the response
                return ResponseEntity.status(200).body(new Response(result.getOutput(), result.getExpectedOutput(), statusResponse, date));
            } catch (IOException e) {
                throw new CompilerException(imageName + "||" + e.getMessage());
            } finally {
                // Ensure the files are deleted after the execution
                FileHandler.deleteFile(folder, file);
                FileHandler.deleteFile(folder, outputFile.getOriginalFilename());
                if (inputFile != null) {
                    FileHandler.deleteFile(folder, inputFile.getOriginalFilename());
                }
                log.info("{}|| Files have been deleted", imageName);
            }
        }

        /*synchronized (this) {

            createEntryPoint(sourceCode, inputFile, language, timeLimit, memoryLimit);

            log.info("{}|| entryPoint file has been created", imageName);

            try {
                FileHandler.saveUploadedFile(sourceCode, folder + "/" + file);
                FileHandler.saveUploadedFile(outputFile, folder + "/" + outputFile.getOriginalFilename());
                if (inputFile!= null) {
                    FileHandler.saveUploadedFile(inputFile, folder + "/" + inputFile.getOriginalFilename());
                }
                log.info("{}|| File have been saved", imageName);
            } catch (IOException e) {
                throw new CompilerException(imageName + "||" + e.getMessage());
            }


            try {
                log.info("{}|| Start building image", imageName);
                AtomicInteger status = new AtomicInteger(dockerService.buildImage(folder, imageName));
                if (status.get() == 0) {
                    log.info("{}|| Image has been built", imageName);
                } else {
                    throw new DockerException(imageName + "|| Error while building image");
                }
            } finally {
                FileHandler.deleteFile(folder, file);
                FileHandler.deleteFile(folder, outputFile.getOriginalFilename());
                if (inputFile!= null)
                    FileHandler.deleteFile(folder, inputFile.getOriginalFilename());
                log.info("{}|| Files have been deleted", imageName);
            }
        }

        Result result = dockerService.executeCode(imageName, outputFile);

        String status = result.getStatus();
        log.info("Status response is{}", status);
        return ResponseEntity.status(200).body(new Response(result.getOutput(), result.getExpectedOutput(), status,date));
         */

    }

    private void createEntryPoint(MultipartFile sourceCode, MultipartFile inputFile,
                                  Language language, int timeLimit, int memoryLimit) {
        switch (language) {
            case CPP -> EntryPointFile.CppEntry(timeLimit, memoryLimit, inputFile);
            case JAVA -> EntryPointFile.JavaEntry(sourceCode.getOriginalFilename(), timeLimit, memoryLimit, inputFile);
            case PYTHON -> EntryPointFile.PythonEntry(timeLimit, memoryLimit, inputFile);
        }
    }
}
