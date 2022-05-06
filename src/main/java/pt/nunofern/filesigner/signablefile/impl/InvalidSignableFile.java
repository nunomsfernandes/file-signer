package pt.nunofern.filesigner.signablefile.impl;

import lombok.RequiredArgsConstructor;
import pt.nunofern.filesigner.exceptions.InvalidFileTypeException;
import pt.nunofern.filesigner.signablefile.ISignableFile;
import pt.nunofern.filesigner.signedfile.SignedFileFactory;
import pt.nunofern.filesigner.signedfile.impl.SignedFile;

import java.nio.file.Path;

@RequiredArgsConstructor
public class InvalidSignableFile implements ISignableFile {

    private final Path path;
    private final SignedFileFactory signedFileFactory;
    private final Exception exception;

    @Override
    public SignedFile sign() {
        return this.signedFileFactory.error(path, new InvalidFileTypeException(exception, exception.getClass().getName()));
    }
}
