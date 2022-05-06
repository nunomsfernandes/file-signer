package pt.nunofern.filesigner.keystore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.nunofern.filesigner.exceptions.LoadCertificateAndPrivateKeyException;
import pt.nunofern.filesigner.model.PrivateKeyAndCertificateChain;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class KeyStoreCertificateAndPrivateKeyLoaderTest {

    KeyStoreConfig validKeyStoreConfig;
    KeyStoreConfig missingKeyStoreConfig;
    KeyStoreConfig invalidPasswordKeyStoreConfig;
    KeyStoreConfig invalidAliasKeyStoreConfig;

    @BeforeEach
    void setup() {
        validKeyStoreConfig = new KeyStoreConfig("classpath:keystore-dev-tests.jks", KeyStoreType.JKS, "nunofernandes", "nunofernandes");
        missingKeyStoreConfig = new KeyStoreConfig("classpath:keystore-dev-tests-invalid.jks", KeyStoreType.JKS, "nunofernandes", "nunofernandes");
        invalidPasswordKeyStoreConfig = new KeyStoreConfig("classpath:keystore-dev-tests.jks", KeyStoreType.JKS, "nunofernandes-invalid", "nunofernandes");
        invalidAliasKeyStoreConfig = new KeyStoreConfig("classpath:keystore-dev-tests.jks", KeyStoreType.JKS, "nunofernandes", "nunofernandes-invalid");
    }

    @Test
    void loadValidPrivateKeyAndCertificateChainSuccessTest() throws LoadCertificateAndPrivateKeyException {
        KeyStoreCertificateAndPrivateKeyLoader loader = new KeyStoreCertificateAndPrivateKeyLoader(this.validKeyStoreConfig);
        PrivateKeyAndCertificateChain pkcc = loader.load();
        System.out.println("TESTE");
        assert (pkcc != null);
    }

    @Test
    void loadMissingKeyStoreErrorTest() {
        KeyStoreCertificateAndPrivateKeyLoader loader = new KeyStoreCertificateAndPrivateKeyLoader(this.missingKeyStoreConfig);
        LoadCertificateAndPrivateKeyException ex = assertThrows(LoadCertificateAndPrivateKeyException.class, () -> {
            PrivateKeyAndCertificateChain pkcc = loader.load();
        });
        assertTrue(ex.getCode().equals("KeyStoreNotFoundException"));
    }

    @Test
    void loadInvalidPasswordKeyStoreErrorTest() {
        KeyStoreCertificateAndPrivateKeyLoader loader = new KeyStoreCertificateAndPrivateKeyLoader(this.invalidPasswordKeyStoreConfig);
        LoadCertificateAndPrivateKeyException ex = assertThrows(LoadCertificateAndPrivateKeyException.class, () -> {
            PrivateKeyAndCertificateChain pkcc = loader.load();
        });
        assertTrue(ex.getCode().equals("LoadKeyStoreErrorException"));
    }

    @Test
    void loadInvalidAliasKeyStoreErrorTest() {
        KeyStoreCertificateAndPrivateKeyLoader loader = new KeyStoreCertificateAndPrivateKeyLoader(this.invalidAliasKeyStoreConfig);
        LoadCertificateAndPrivateKeyException ex = assertThrows(LoadCertificateAndPrivateKeyException.class, () -> {
            PrivateKeyAndCertificateChain pkcc = loader.load();
        });
        assertTrue(ex.getCode().equals("KeyStoreAliasNotFoundException"));
    }


}