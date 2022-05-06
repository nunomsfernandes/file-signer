package pt.nunofern.filesigner.signablefile;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;


public class SignableFileTypeResolver {


    public static SignableFileBuilder resolve(Path path) {
        String fileExtension = FilenameUtils.getExtension(path.getFileName().toString());
        return SignableFileBuilder.valueOf(fileExtension.toUpperCase());
    }
}
