package ru.webency.compiler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Response {
    private String output;
    private String expectedOutput;
    private String status;
    private LocalDateTime date;
}
