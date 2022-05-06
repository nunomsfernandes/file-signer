package pt.nunofern.filesigner.keystore;

import pt.nunofern.filesigner.exceptions.LoadCertificateAndPrivateKeyException;
import pt.nunofern.filesigner.model.PrivateKeyAndCertificateChain;

public interface ICertificateAndPrivateKeyLoader {

    PrivateKeyAndCertificateChain load() throws LoadCertificateAndPrivateKeyException;

}
