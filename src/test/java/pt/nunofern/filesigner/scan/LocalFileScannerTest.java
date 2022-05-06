package pt.nunofern.filesigner.scan;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import pt.nunofern.filesigner.exceptions.ScanFileException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LocalFileScannerTest {

    @Test
    void scanSuccessTest() throws ScanFileException, IOException, URISyntaxException {
        File validImportDirectoryFile = ResourceUtils.getFile("classpath:import");
        IFileScanner scanner = new LocalFolderFileScanner(validImportDirectoryFile.getAbsolutePath());
        assert (scanner.scan().count() == 4);
    }

    @Test
    void scanNoSuchDirectoryErrorTest() throws URISyntaxException {
        IFileScanner scanner = new LocalFolderFileScanner("some_invalid_directory/");
        ScanFileException ex = assertThrows(ScanFileException.class, () -> {
            scanner.scan();
        });
        assertTrue(ex.getCode().equals("NoSuchFileException"));
    }

    @Test
    void scanFileErrorTest() throws FileNotFoundException, URISyntaxException {
        File validImportDirectoryFile = ResourceUtils.getFile("import/Doc1.pdf");
        IFileScanner scanner = new LocalFolderFileScanner(validImportDirectoryFile.getAbsolutePath());
        ScanFileException ex = assertThrows(ScanFileException.class, () -> {
            scanner.scan();
        });
        assertTrue(ex.getCode().equals("NoSuchFileException"));
    }

}