package pt.nunofern.filesigner.signablefile.impl;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.*;
import org.springframework.util.CollectionUtils;
import pt.nunofern.filesigner.exceptions.SignFileException;
import pt.nunofern.filesigner.model.SignatureMetadata;
import pt.nunofern.filesigner.signablefile.ISignableFile;
import pt.nunofern.filesigner.signablefile.signer.IFileSigner;
import pt.nunofern.filesigner.signedfile.SignedFileFactory;
import pt.nunofern.filesigner.signedfile.impl.SignedFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class PdfSignableFile implements ISignableFile {

    private final Path path;
    private final IFileSigner fileSigner;
    private final static String CONF_TRY_FIX_CORRUPTED_FILES = "try-fix-corrupted-files";
    private final SignedFileFactory signedFileFactory;
    private final static String CONF_TRY_FIX_CORRUPTED_FILES_TEMP_PATH = "try-fix-corrupted-files-temp-path";
    private final SignatureMetadata metadata;
    private final Boolean tryFixCorruptedFiles;
    private final Path tryFixCorruptedFilesTempPath;

    public PdfSignableFile(Path path, IFileSigner fileSigner, SignatureMetadata metadata, SignedFileFactory signedFileFactory, Map<String, String> config) {
        this.path = path;
        this.fileSigner = fileSigner;
        this.metadata = metadata;
        this.signedFileFactory = signedFileFactory;
        this.tryFixCorruptedFiles = Boolean.valueOf(config.get(CONF_TRY_FIX_CORRUPTED_FILES));
        this.tryFixCorruptedFilesTempPath = Path.of(config.get(CONF_TRY_FIX_CORRUPTED_FILES_TEMP_PATH));
    }


    //private final Path workingTempRelativePath = Path.of("~temp");

    @Override
    public SignedFile sign() {
        PDDocument document = null;
        SignatureOptions options = null;
        ByteArrayOutputStream signedDataStream = new ByteArrayOutputStream();
        ExternalSigningSupport externalSigning = null;
        try {
            document = PDDocument.load(readFile(this.path));
            PDSignature signature = createPDSignature(this.metadata);
            options = createSignatureOptions();
            document.addSignature(signature, options);
            signedDataStream = new ByteArrayOutputStream();
            externalSigning = document.saveIncrementalForExternalSigning(signedDataStream);
            byte[] signableHashData = IOUtils.toByteArray(externalSigning.getContent());
            byte[] signedHashData = this.fileSigner.sign(signableHashData);
            externalSigning.setSignature(signedHashData);
            return this.signedFileFactory.success(this.path, signedDataStream.toByteArray());
        } catch (IOException | SignFileException e) {
            return this.signedFileFactory.error(this.path, e);
        } finally {
            IOUtils.closeQuietly(signedDataStream, null);
            IOUtils.closeQuietly(document, null);
            IOUtils.closeQuietly(options, null);
        }
    }

    private byte[] readFile(Path path) throws IOException {
        return this.tryFixCorruptedFiles ? readTemporaryFile(path) : readOriginalFile(path);
    }

    private byte[] readOriginalFile(Path path) throws IOException {
        return readFileInternal(path);
    }

    private byte[] readTemporaryFile(Path path) throws IOException {
        Path temporaryFilePath = createTemporaryFile(path);
        byte[] bytes = readFileInternal(temporaryFilePath);
        cleanUpTemporaryFile(path);
        return bytes;
    }

    private byte[] readFileInternal(Path path) throws IOException {
        InputStream is = null;
        try {
            is = createInputStreamForPath(path);
            return IOUtils.toByteArray(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private InputStream createInputStreamForPath(Path path) throws IOException {
        return Files.newInputStream(path, StandardOpenOption.READ);
    }

    private PDSignature createPDSignature(SignatureMetadata info) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        Calendar calendar = GregorianCalendar.from(zdt);

        PDSignature signature = new PDSignature();
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
        signature.setSignDate(calendar);
        signature.setName(info.name());
        signature.setLocation(info.location());
        signature.setReason(info.reason());
        signature.setPropBuild(new PDPropBuild());
        signature.getPropBuild().setPDPropBuildApp(new PDPropBuildDataDict());
        signature.getPropBuild().getApp().setName(info.appName());
        return signature;
    }

    private SignatureOptions createSignatureOptions() {
        SignatureOptions options = new SignatureOptions();
        return options;
    }

    /*
     Because some pdfs are invalid(something related with the length of the stream inside the pdf itself)
     we need to open the pdf, parse it, and save it to another location. This new pdf will be used
     only for signing, the resulted byte[] will be from this temporary pdf.
     If the file doesn't have any signature we can build a pdf from scratch, otherwise we made a saveIncremental to not
     invalidate previous signatures
  */
    private Path createTemporaryFile(Path path) throws IOException {
        PDDocument document = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            inputStream = createInputStreamForPath(path);
            document = PDDocument.load(inputStream);
            if (hasSignatures(document)) {
                document.saveIncremental(outputStream);
            } else {
                document.save(outputStream);
            }
            Path workingTempFilePath = resolveWorkingTempFilePath(path);
            byte[] bytes = outputStream.toByteArray();
            Files.createDirectories(workingTempFilePath.getParent());
            Files.write(workingTempFilePath, bytes);
            return workingTempFilePath;
        } finally {
            IOUtils.closeQuietly(inputStream, null);
            IOUtils.closeQuietly(document, null);
        }
    }

    private boolean hasSignatures(PDDocument document) throws IOException {
        return !CollectionUtils.isEmpty(document.getSignatureDictionaries());
    }

    private void cleanUpTemporaryFile(Path path) throws IOException {
        Path workingTempFilePath = resolveWorkingTempFilePath(path);
        //Files.delete(workingTempFilePath);
    }

    private Path resolveWorkingTempFilePath(Path path) {
        Path workingTempFilePath = this.tryFixCorruptedFilesTempPath.resolve(path.getFileName());
        return workingTempFilePath;
    }

}
