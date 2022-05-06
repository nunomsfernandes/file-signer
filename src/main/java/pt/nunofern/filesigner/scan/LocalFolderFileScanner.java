package pt.nunofern.filesigner.scan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pt.nunofern.filesigner.exceptions.ScanFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


@Component
public class LocalFolderFileScanner implements IFileScanner {

    private final Path pathToScan;

    public LocalFolderFileScanner(@Value("${import.directory}") String folderToScan) {
        this.pathToScan = Paths.get(folderToScan);
    }

    @Override
    public Stream<Path> scan() throws ScanFileException {
        try {
            return Files.list(this.pathToScan).filter(path -> !Files.isDirectory(path));
        } catch (IOException e) {
            throw new ScanFileException(e, e.getClass().getSimpleName());
        }
    }

}
