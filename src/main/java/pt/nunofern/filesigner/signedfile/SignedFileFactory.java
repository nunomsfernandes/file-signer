package pt.nunofern.filesigner.signedfile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pt.nunofern.filesigner.signedfile.impl.FailedSignedFile;
import pt.nunofern.filesigner.signedfile.impl.SignedFile;
import pt.nunofern.filesigner.signedfile.impl.SuccessSignedFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SignedFileFactory {

    private final Path successSignedDestinationPath;
    private final Path failSignedDestinationPath;

    public SignedFileFactory(@Value("${export.directory.success}") String successSignedDestinationPath,
                             @Value("${export.directory.fail}") String failSignedDestinationPath) {
        this.successSignedDestinationPath = Paths.get(successSignedDestinationPath);
        this.failSignedDestinationPath = Paths.get(failSignedDestinationPath);
    }

    public SignedFile success(Path originalPath, byte[] bytes) {
        return new SuccessSignedFile(originalPath, this.successSignedDestinationPath, bytes);
    }

    public SignedFile error(Path originalPath, Exception exception) {
        return new FailedSignedFile(originalPath, this.failSignedDestinationPath, exception);
    }

}
