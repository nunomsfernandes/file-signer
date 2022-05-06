package pt.nunofern.filesigner.exceptions;

import lombok.Getter;

public class LoadCertificateAndPrivateKeyException extends Exception {

    @Getter
    private final String code;

    public LoadCertificateAndPrivateKeyException(String code) {
        this.code = code;
    }

    public LoadCertificateAndPrivateKeyException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
