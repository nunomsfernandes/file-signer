package pt.nunofern.filesigner.exceptions;

import lombok.Getter;

public class ScanFileException extends Exception {

    @Getter
    private final String code;

    public ScanFileException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}