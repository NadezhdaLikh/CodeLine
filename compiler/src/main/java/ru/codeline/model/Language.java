package ru.webency.compiler.model;

import lombok.Getter;

@Getter
public enum Language {
    PYTHON("container_py", "main.py", "python3"),
    CPP("container_cpp", "main.cpp", "g++"),
    JAVA("container_java", "main.java", "javac");

    final String folder;
    final String file;
    final String command;

    Language(String folder, String file, String command) {
        this.folder = folder;
        this.file = file;
        this.command = command;
    }

    public static Language fromString(String languageStr) {
        return switch (languageStr) {
            case "Python" -> PYTHON;
            case "C++" -> CPP;
            case "Java" -> JAVA;
            default -> throw new IllegalArgumentException("Unknown language: " + languageStr);
        };
    }
}

