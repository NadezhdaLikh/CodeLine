package ru.codeline.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccessDeniedException extends Exception {
    private final String message;
}
