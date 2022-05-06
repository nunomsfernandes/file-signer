package pt.nunofern.filesigner.signedfile;

import java.io.IOException;
import java.nio.file.Path;

public interface ISignedFile {

    void moveToDestination() throws IOException;

    void beforeMoveToDestination();

    void afterMoveToDestination(Path destinationPath) throws IOException;

}
