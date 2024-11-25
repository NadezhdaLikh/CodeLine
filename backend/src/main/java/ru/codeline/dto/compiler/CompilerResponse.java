package ru.codeline.dto.compiler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilerResponse {
    private String output;
    private String expectedOutput;
    private String status;
    private LocalDateTime date;
}
