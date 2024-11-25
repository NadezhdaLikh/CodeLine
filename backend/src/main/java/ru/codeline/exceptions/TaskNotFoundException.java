package ru.codeline.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskNotFoundException extends Exception {
    private final String message;
}
