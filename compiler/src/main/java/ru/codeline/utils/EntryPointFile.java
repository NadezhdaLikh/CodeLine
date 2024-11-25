package ru.webency.compiler.utils;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import ru.webency.compiler.model.Language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Slf4j
public class EntryPointFile {
    private static final String TIMEOUT_CMD = "timeout --signal=SIGTERM ";
    private static final String BASH_HEADER = "#!/usr/bin/env bash\n";

    private EntryPointFile() {};


    @SneakyThrows
    public static void PythonEntry(int timeLimit, int memoryLimit, MultipartFile inputFile) {
        String executionCommand = inputFile == null
                ? TIMEOUT_CMD + timeLimit + "s " + Language.PYTHON.getCommand() + " main.py" + "\n"
                : TIMEOUT_CMD+ timeLimit + "s " + Language.PYTHON.getCommand() + " main.py" + " < " + inputFile.getOriginalFilename() + "\n";

        String content = BASH_HEADER +
                "ulimit -s " + memoryLimit + "\n" +
                executionCommand +
                "exit $?\n";

        try(OutputStream os = new FileOutputStream(new File(Language.PYTHON.getFolder() + "/entrypoint.sh"))) {
            os.write(content.getBytes(), 0, content.length());
        }
    }

    @SneakyThrows
    public static void JavaEntry(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {

        String executionCommand = inputFile == null
                ? TIMEOUT_CMD + timeLimit + " java " + fileName.substring(0, fileName.length() - 5) + "\n"
                : TIMEOUT_CMD + timeLimit + " java " + fileName.substring(0, fileName.length() - 5) + " < " + inputFile.getOriginalFilename() + "\n";

        String content = BASH_HEADER +
                "mv main.java " + fileName + "\n" +
                Language.JAVA.getCommand() + " " + fileName + "\n" +
                "ret=$?\n" +
                "if [ $ret -ne 0 ]\n" +
                "then\n" +
                "  exit 2\n" +
                "fi\n" +
                "ulimit -s " + memoryLimit + "\n" +
                executionCommand +
                "exit $?\n";

        try(OutputStream os = new FileOutputStream(new File(Language.JAVA.getFolder() + "/entrypoint.sh"))) {
            os.write(content.getBytes(), 0, content.length());
        }
    }

    @SneakyThrows
    public static void CppEntry(int timeLimit, int memoryLimit, MultipartFile inputFile) {

        String executionCommand = inputFile == null
                ? TIMEOUT_CMD + timeLimit + " ./exec " + "\n"
                : TIMEOUT_CMD + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";

        String content = BASH_HEADER +
                Language.CPP.getCommand() + " main.cpp" + " -o exec" + "\n" +
                "ret=$?\n" +
                "if [ $ret -ne 0 ]\n" +
                "then\n" +
                "  exit 2\n" +
                "fi\n" +
                "ulimit -s " + memoryLimit + "\n" +
                executionCommand +
                "exit $?\n";

        try(OutputStream os = new FileOutputStream(new File(Language.CPP.getFolder() + "/entrypoint.sh"))) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
}
