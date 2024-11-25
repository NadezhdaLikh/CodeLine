package ru.codeline.utils;

public class StatusMessage {
    public static String statusResponse(int status, boolean result) {
        switch (status) {
            case 0:
                if (result) {
                    return "Успешно";
                } else {
                    return "Не правильный ответ";
                }
            case 2:
                return "Ошибка компиляции";
            case 1:
                return "Ошибка во время выполнения";
            case 139:
                return "Превышен лимит по памяти";
            default:
                return "Превышен лимит по времени выполнения";
        }
    }
}
