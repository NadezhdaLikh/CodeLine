package ru.codeline.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LectureNotFoundException extends Exception {
    private final String message;
}
