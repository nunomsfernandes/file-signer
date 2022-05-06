package pt.nunofern.filesigner.keystore;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import pt.nunofern.filesigner.exceptions.LoadCertificateAndPrivateKeyException;
import pt.nunofern.filesigner.model.PrivateKeyAndCertificateChain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class KeyStoreCertificateAndPrivateKeyLoader implements ICertificateAndPrivateKeyLoader {

    private final KeyStoreConfig keyStoreConfig;

    public PrivateKeyAndCertificateChain load() throws LoadCertificateAndPrivateKeyException {
        try {
            KeyStore keyStore = KeyStore.getInstance(this.keyStoreConfig.keyStoreType().name());
            FileInputStream fis = openFileInputStream();
            loadKeyStore(keyStore, fis);
            Certificate[] certificateChain = Optional.ofNullable(keyStore.getCertificateChain(this.keyStoreConfig.alias()))
                    .orElseThrow(() -> (new LoadCertificateAndPrivateKeyException("KeyStoreAliasNotFoundException")));
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(this.keyStoreConfig.alias(), this.keyStoreConfig.keyStorePassword().toCharArray());
            return new PrivateKeyAndCertificateChain(privateKey, certificateChain);
        } catch (KeyStoreException | UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException e) {
            throw new LoadCertificateAndPrivateKeyException(e, e.getClass().getSimpleName());
        }
    }

    private FileInputStream openFileInputStream() throws LoadCertificateAndPrivateKeyException {
        FileInputStream fileStream = null;
        try {
            File file = ResourceUtils.getFile(this.keyStoreConfig.keyStore());
            fileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new LoadCertificateAndPrivateKeyException(e, "KeyStoreNotFoundException");
        }
        return fileStream;
    }

    private void loadKeyStore(KeyStore keyStore, FileInputStream fileStream) throws NoSuchAlgorithmException, CertificateException, LoadCertificateAndPrivateKeyException {
        try {
            keyStore.load(fileStream, this.keyStoreConfig.keyStorePassword().toCharArray());
        } catch (IOException e) {
            throw new LoadCertificateAndPrivateKeyException(e, "LoadKeyStoreErrorException");
        }
    }

}
