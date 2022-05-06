package pt.nunofern.filesigner.signedfile.impl;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import pt.nunofern.filesigner.signedfile.ISignedFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;


public abstract class SignedFile implements ISignedFile {

    @Getter
    private final Path originalPath;
    @Getter
    private final Path destinationPath;

    protected SignedFile(Path originalPath, Path destinationPath) {
        this.originalPath = originalPath;
        this.destinationPath = destinationPath;
    }

    @Override
    public void moveToDestination() throws IOException {
        beforeMoveToDestination();
        Path destinationFilePath = getDestinationPath().resolve(resolveDestinationFileName(getOriginalPath()));
        moveToDestination(destinationFilePath);
        afterMoveToDestination(destinationFilePath);
    }

    private String resolveDestinationFileName(Path originalPath) {
        String fileNameWithExtension = getOriginalPath().getFileName().toString();
        String fileBaseName = FilenameUtils.getBaseName(fileNameWithExtension);
        String fileExtension = FilenameUtils.getExtension(fileNameWithExtension);
        long timestamp = Instant.now().getEpochSecond();
        String destinationFileName = fileBaseName + "_" + timestamp + "." + fileExtension;
        return destinationFileName;
    }

    abstract void moveToDestination(Path destinationPath) throws IOException;

    @Override
    public void beforeMoveToDestination() {

    }

    @Override
    public void afterMoveToDestination(Path destinationPath) throws IOException {

    }
}
