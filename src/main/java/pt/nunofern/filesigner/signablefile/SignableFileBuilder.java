package pt.nunofern.filesigner.signablefile;

import pt.nunofern.filesigner.model.SignatureMetadata;
import pt.nunofern.filesigner.signedfile.SignedFileFactory;
import pt.nunofern.filesigner.signablefile.impl.InvalidSignableFile;
import pt.nunofern.filesigner.signablefile.impl.PdfSignableFile;
import pt.nunofern.filesigner.signablefile.signer.IFileSigner;

import java.nio.file.Path;
import java.util.Map;

public enum SignableFileBuilder {


    PDF {
        @Override
        protected ISignableFile buildSignableFile(Path path, IFileSigner fileSigner, SignatureMetadata metadata, SignedFileFactory signedFileFactory, Map<String, String> config) {
            return new PdfSignableFile(path, fileSigner, metadata, signedFileFactory, config);
        }
    },
    INVALID {
        @Override
        protected ISignableFile buildSignableFile(Path path, IFileSigner fileSigner, SignatureMetadata metadata, SignedFileFactory signedFileFactory, Map<String, String> config) {
            throw new UnsupportedOperationException();
        }
    };

    public static ISignableFile build(Path path, IFileSigner fileSigner, SignatureMetadata metadata, SignedFileFactory signedFileFactory, Map<String, Map<String, String>> config) {
        try {
            SignableFileBuilder b = SignableFileTypeResolver.resolve(path);
            return b.buildSignableFile(path, fileSigner, metadata, signedFileFactory, config.get(b.name()));
        } catch (IllegalArgumentException e) {
            return INVALID.buildError(path, signedFileFactory, e);
        }
    }

    protected abstract ISignableFile buildSignableFile(Path path, IFileSigner fileSigner, SignatureMetadata metadata, SignedFileFactory signedFileFactory, Map<String, String> config);

    protected ISignableFile buildError(Path path, SignedFileFactory signedFileFactory, Exception exception) {
        return new InvalidSignableFile(path, signedFileFactory, exception);
    }

}
