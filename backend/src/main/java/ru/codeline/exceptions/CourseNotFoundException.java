package ru.codeline.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseNotFoundException extends Exception{
    private final String message;
}
