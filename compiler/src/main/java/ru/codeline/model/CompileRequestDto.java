package ru.webency.compiler.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompileRequestDto {
    private String sourceCode;
    private String input;
    private String expectedOutput;
    private String language;
    private int timeLimit;
    private int memoryLimit;
}
