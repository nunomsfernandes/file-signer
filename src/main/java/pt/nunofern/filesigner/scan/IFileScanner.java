package pt.nunofern.filesigner.scan;

import pt.nunofern.filesigner.exceptions.ScanFileException;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IFileScanner {


    Stream<Path> scan() throws ScanFileException;

}
