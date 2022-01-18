package blockchain;

import java.io.Serializable;
import java.security.*;

public class GenerateKeys implements Serializable {

    transient private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys() throws NoSuchAlgorithmException, NoSuchProviderException {

    }

    public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
        createKeys();
    }

    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    PublicKey getPublicKey() {
        return this.publicKey;
    }

}
