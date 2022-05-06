package pt.nunofern.filesigner.signedfile.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FailedSignedFile extends SignedFile {

    private final Exception exception;

    public FailedSignedFile(Path originalPath, Path destinationPath, Exception exception) {
        super(originalPath, destinationPath);
        this.exception = exception;
    }

    @Override
    public void moveToDestination(Path destinationFilePath) throws IOException {
        Files.move(getOriginalPath(), destinationFilePath);
    }

    @Override
    public void afterMoveToDestination(Path destinationPath) throws IOException {
        String destinationFileName = destinationPath.getFileName().toString();
        Path txtErrorFile = destinationPath.getParent().resolve(destinationFileName + ".txt");
        Files.write(txtErrorFile, ExceptionUtils.getStackTrace(this.exception).getBytes());
    }
}
