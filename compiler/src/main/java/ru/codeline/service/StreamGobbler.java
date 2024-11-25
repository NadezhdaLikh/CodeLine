package ru.codeline.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
    private final InputStream inputStream;
    private final StringBuilder content;

    public StreamGobbler(InputStream inputStream) {
        this.inputStream = inputStream;
        this.content = new StringBuilder();
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content.toString();
    }
}
