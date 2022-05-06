package pt.nunofern.filesigner.signablefile.signer;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.stereotype.Component;
import pt.nunofern.filesigner.exceptions.LoadCertificateAndPrivateKeyException;
import pt.nunofern.filesigner.exceptions.SignFileException;
import pt.nunofern.filesigner.keystore.KeyStoreCertificateAndPrivateKeyLoader;
import pt.nunofern.filesigner.model.PrivateKeyAndCertificateChain;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

@Component
public class BouncyCastlePrivateKeyByteArraySigner implements IFileSigner<byte[]> {

    private final CMSSignedDataGenerator gen;

    public BouncyCastlePrivateKeyByteArraySigner(KeyStoreCertificateAndPrivateKeyLoader certificateAndPrivateKeyLoader)
            throws LoadCertificateAndPrivateKeyException, OperatorCreationException, CertificateEncodingException, CMSException {
        PrivateKeyAndCertificateChain pkAndCc = certificateAndPrivateKeyLoader.load();
        X509Certificate cert = (X509Certificate) pkAndCc.certificateChain()[0];
        ContentSigner sha256Signer = new JcaContentSignerBuilder(cert.getSigAlgName())
                .build(pkAndCc.privateKey());
        this.gen = new CMSSignedDataGenerator();
        this.gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().build())
                .build(sha256Signer, cert));
        this.gen.addCertificates(new JcaCertStore(Arrays.asList(pkAndCc.certificateChain())));
    }


    @Override
    public byte[] sign(byte[] toSign) throws SignFileException {
        try {
            CMSProcessableByteArray msg = new CMSProcessableByteArray(toSign);
            CMSSignedData signedData;
            signedData = this.gen.generate(msg, false);
            return signedData.toASN1Structure().getEncoded();
        } catch (CMSException | IOException e) {
            throw new SignFileException(e, e.getClass().getSimpleName());
        }

    }

}
