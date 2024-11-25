package ru.webency.compiler.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DockerCommandLine {
    private DockerCommandLine(){}
    public static String executeCommand(String ...params) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(params);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return readOutput(reader);
    }
    public static String readOutput(BufferedReader reader) throws IOException {
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}
