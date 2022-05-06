package pt.nunofern.filesigner.keystore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record KeyStoreConfig(String keyStore,
                             KeyStoreType keyStoreType,
                             String keyStorePassword,
                             String alias) {


    public KeyStoreConfig(@Value("${keystore.config.uri}") String keyStore,
                          @Value("${keystore.config.type}") KeyStoreType keyStoreType,
                          @Value("${keystore.config.password}") String keyStorePassword,
                          @Value("${keystore.config.certificate.alias}") String alias) {
        this.keyStore = keyStore;
        this.keyStoreType = keyStoreType;
        this.keyStorePassword = keyStorePassword;
        this.alias = alias;
    }
}
