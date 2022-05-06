package pt.nunofern.filesigner.exceptions;

import lombok.Getter;

public class SignFileException extends Exception {

    @Getter
    private final String code;

    public SignFileException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
