package pt.nunofern.filesigner.signedfile.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SuccessSignedFile extends SignedFile {

    private final byte[] bytes;

    public SuccessSignedFile(Path originalPath, Path destinationPath, byte[] bytes) {
        super(originalPath, destinationPath);
        this.bytes = bytes;
    }

    @Override
    public void moveToDestination(Path destinationFilePath) throws IOException {
        Files.write(destinationFilePath, bytes);
        Files.delete(getOriginalPath());
    }
}
