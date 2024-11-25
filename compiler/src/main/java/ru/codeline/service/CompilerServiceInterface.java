package ru.codeline.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.codeline.exceptions.CompilerException;
import ru.codeline.model.Language;
import ru.codeline.model.Request;

import java.io.IOException;

public interface CompilerServiceInterface {
    ResponseEntity<Object> compile(
            MultipartFile sourceCode,
            MultipartFile inputFile,
            MultipartFile outputFile,
            Language language,
            int timeLimit,
            int memoryLimit) throws CompilerException;

    ResponseEntity<Object> compile(Request request) throws CompilerException, IOException;
}
