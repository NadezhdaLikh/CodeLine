package ru.codeline.model;

public enum ProblemStatus {
    ACCEPTED("Успешно"),
    WRONG_ANSWER("Не правильный ответ"),
    TIME_LIMIT_EXCEEDED("Превышен лимит по времени выполнения"),
    MEMORY_LIMIT_EXCEEDED("Превышен лимит по памяти"),
    COMPILATION_ERROR("Ошибка во время компиляции"),
    RUNTIME_ERROR("Ошибка во время выполнения");
    private final String value;
    ProblemStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
