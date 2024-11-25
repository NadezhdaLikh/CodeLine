package ru.codeline.dto.compiler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilerRequest {
    private String sourceCode;
    private String input;
    private String expectedOutput;
    private String language;
}
