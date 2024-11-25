package ru.webency.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.webency.compiler.model.Result;
import ru.webency.compiler.utils.DockerCommandLine;
import ru.webency.compiler.utils.StatusMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DockerService implements DockerServiceInterface {
    private static final long TIME_OUT = 20000;
    private static final int TIME_LIMIT_STATUS_CODE = 124;

    @Override
    public int buildImage(String folder, String imageName) {
        try {
            String[] command = new String[]{"docker", "image", "build", folder, "-t", imageName};
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            return process.waitFor();
        } catch (Exception e) {
            log.error("Error during the build image process: ", e);
            return 1;
        }

    }

    @Override
    public Result executeCode(String imageName, MultipartFile outputFile) {
        try {
            int status;

            BufferedReader expectedOutputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
            String expectedOutput = DockerCommandLine.readOutput(expectedOutputReader);

            log.info(imageName + "|| Running the container");
            String[] dockerCommand = new String[]{"docker", "run", "--rm", imageName};
            ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
            Process process = processbuilder.start();

            // Read the output/error stream asynchronously
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
            outputGobbler.start();
            errorGobbler.start();


            process.waitFor(TIME_OUT, TimeUnit.MILLISECONDS);

            if (process.isAlive()) {
                status = TIME_LIMIT_STATUS_CODE;
                log.info(imageName + "|| The container exceed the 20 sec allowed for its execution");
                process.destroy();
                log.info(imageName + "|| The container has been destroyed");

                String statusResponse = StatusMessage.statusResponse(status, false);
                return new Result(statusResponse, "No available output", expectedOutput);
            } else {
                status = process.exitValue();
                log.info(imageName + "|| End of the execution of the container");

                BufferedReader containerOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String containerOutput = DockerCommandLine.readOutput(containerOutputReader);

                boolean result = compareResult(containerOutput, expectedOutput);
                String statusResponse = StatusMessage.statusResponse(status, result);

                return new Result(statusResponse, containerOutput, expectedOutput);
            }
        } catch (Exception e){
            log.error("Error: ", e);
            return new Result(StatusMessage.statusResponse(1, false), "A server side error occurred", "");
        }
    }

    @Override
    public String getRunningContainers() throws IOException {
        return DockerCommandLine.executeCommand("docker", "ps");
    }

    @Override
    public String getRunningImages() throws IOException {
        return DockerCommandLine.executeCommand("docker", "image");
    }
    @Override
    public String deleteImage(String imageName) throws IOException {
        return DockerCommandLine.executeCommand("docker", "rmi", "-f", imageName);
    }

    private boolean compareResult(String containerOutput, String expectedOutput) {
        return containerOutput
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("/n","")
                .equals(expectedOutput
                        .trim()
                        .replaceAll("\\s+", " ")
                        .replaceAll("/n", ""));
    }
}
