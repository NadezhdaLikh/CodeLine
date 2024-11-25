package ru.codeline.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuizNotFoundException extends Exception {
    private final String message;
}
