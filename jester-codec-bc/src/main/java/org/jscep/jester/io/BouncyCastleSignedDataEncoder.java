package org.jscep.jester.io;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSAbsentContent;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.util.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

public class BouncyCastleSignedDataEncoder implements EntityEncoder<List<X509Certificate>> {
    private final CMSSignedDataGenerator signedDataGenerator;

    public BouncyCastleSignedDataEncoder(CMSSignedDataGenerator signedDataGenerator) {
        this.signedDataGenerator = signedDataGenerator;
    }

    @Override
    public void encode(List<X509Certificate> entity, OutputStream out) throws IOException {
        try {
            Store store = new JcaCertStore(entity);
            signedDataGenerator.addCertificates(store);
            CMSSignedData signedData = signedDataGenerator.generate(new CMSAbsentContent());

            out.write(signedData.getEncoded());
        } catch (CMSException e) {
            throw new IOException(e);
        } catch (CertificateEncodingException e) {
            throw new IOException(e);
        }
    }
}