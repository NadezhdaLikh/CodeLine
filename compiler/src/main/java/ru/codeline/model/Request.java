package ru.codeline.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private String sourceCode;
    private String input;
    private String expectedOutput;
    private Language language;
    private int timeLimit;
    private int memoryLimit;

    public MultipartFile getSourceCode() throws IOException {
        // Construct the path to the source code file based on the language settings
        File sourceCodeFile = new File(language.getFolder() + "/" + language.getFile());

        // Create and return a new MultipartFile with the source code content
        return new MockMultipartFile(
                language.getFile(),
                language.getFile(),
                null ,
                new ByteArrayInputStream(this.sourceCode.getBytes()));
    }

    public MultipartFile getInput() throws IOException {
        if (this.input == null) {
            return null;
        }
        // Construct the path to the input file
        File input = new File(language.getFolder() + "/input.txt");

        // Create and return a new MultipartFile with the input content
        return new MockMultipartFile(
                // This parameter represents the name of the form field in which the file is being uploaded
                // In the context of file uploads, this is the field name that would be used in a multipart form-data request
                "input.txt",
                // This parameter represents the original filename of the uploaded file as it was on the client's filesystem
                // This is the name the file had when it was selected for upload
                "input.txt",
                null,
                new ByteArrayInputStream(this.input.getBytes()));
    }

    public MultipartFile getExpectedOutput() throws IOException {
        // Construct the path to the expected output file
        File expectedOutput = new File(language.getFolder() + "/expectedOutput.txt");

        // Create and return a new MultipartFile with the expected output content
        return new MockMultipartFile(
                "expectedOutput.txt",
                "expectedOutput.txt",
                null,
                new ByteArrayInputStream(this.expectedOutput.getBytes()));
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Request)) {
            return false;
        }
        Request request = (Request) o;

        if (request.input != this.input && ((this.input != null && !this.input.equals("")) || (request.input != null && !request.input.equals("")))) {
            return false;
        }

        return this.language.equals(request.language)
                && this.expectedOutput.equals(request.expectedOutput)
                && this.memoryLimit == request.memoryLimit
                && this.timeLimit == request.timeLimit
                && this.sourceCode.equals(this.sourceCode);
    }

}
