package ru.webency.compiler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Result {
    private String status;
    private String output;
    private String expectedOutput;
}
