package pt.nunofern.filesigner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.nunofern.filesigner.exceptions.InitializeApplicationException;
import pt.nunofern.filesigner.exceptions.ScanFileException;
import pt.nunofern.filesigner.model.SignatureMetadata;
import pt.nunofern.filesigner.signablefile.ISignableFile;
import pt.nunofern.filesigner.signablefile.SignableFileBuilder;
import pt.nunofern.filesigner.signablefile.signer.IFileSigner;
import pt.nunofern.filesigner.signedfile.ISignedFile;
import pt.nunofern.filesigner.signedfile.SignedFileFactory;
import pt.nunofern.filesigner.signedfile.impl.SignedFile;
import pt.nunofern.filesigner.scan.IFileScanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@SpringBootApplication
@RequiredArgsConstructor
public class FileSignerApplication implements CommandLineRunner {

    private final IFileSigner fileSigner;
    private final IFileScanner notProcessedFilesScanner;
    private final SignatureMetadata metadata;
    private final SignedFileFactory signedFileFactory;

    private final ApplicationConfiguration applicationConfig;

    public static void main(String[] args) {
        SpringApplication.run(FileSignerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            //Scan
            Stream<Path> pathStream = this.notProcessedFilesScanner.scan();
            //Load files
            Stream<ISignableFile> signableFileStream = pathStream.map(path -> buildSignableFile(path));
            //Sign files
            Stream<SignedFile> signedFileStream = signableFileStream.map(signableFile -> signableFile.sign());
            //Export
            signedFileStream.forEach(sf -> moveSignedFileToDestination(sf));
        } catch (ScanFileException e) {
            throw new InitializeApplicationException(e);
        }
    }

    private ISignableFile buildSignableFile(Path path) {
        return SignableFileBuilder.build(path, this.fileSigner, this.metadata, this.signedFileFactory,
                this.applicationConfig.getSignablefileConfig());
    }

    private void moveSignedFileToDestination(ISignedFile signedFile) {
        try {
            signedFile.moveToDestination();
        } catch (IOException e) {
            throw new InitializeApplicationException(e);
        }
    }

}
