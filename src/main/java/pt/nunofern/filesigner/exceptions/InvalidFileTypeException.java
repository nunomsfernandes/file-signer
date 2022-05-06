package pt.nunofern.filesigner.exceptions;

import lombok.Getter;

public class InvalidFileTypeException extends Exception {

    @Getter
    private final String code;

    public InvalidFileTypeException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}

