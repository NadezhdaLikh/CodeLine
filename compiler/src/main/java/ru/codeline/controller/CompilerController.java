package ru.codeline.controller;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.codeline.exceptions.CompilerException;
import ru.codeline.model.Language;
import ru.codeline.model.Request;
import ru.codeline.service.CompilerServiceInterface;
import ru.codeline.model.CompileRequestDto;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/compiler")
public class CompilerController {
    private CompilerServiceInterface compilerService;

    @PostMapping
    public ResponseEntity<Object> compile(@RequestParam Integer timeLimit,
                                          @RequestParam Integer memoryLimit,
                                          @RequestBody CompileRequestDto compileRequestDto)
            throws CompilerException, IOException {
        Language language = Language.fromString(compileRequestDto.getLanguage());
        Request request = new Request(
                compileRequestDto.getSourceCode(),
                compileRequestDto.getInput(),
                compileRequestDto.getExpectedOutput(),
                language,
                timeLimit,
                memoryLimit
        );
        return compilerService.compile(request);
    }
}
