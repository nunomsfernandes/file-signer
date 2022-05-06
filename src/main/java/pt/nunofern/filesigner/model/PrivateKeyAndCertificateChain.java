package pt.nunofern.filesigner.model;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public record PrivateKeyAndCertificateChain(PrivateKey privateKey, Certificate[] certificateChain) {

}
