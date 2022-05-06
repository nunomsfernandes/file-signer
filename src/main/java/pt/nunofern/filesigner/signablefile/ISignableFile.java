package pt.nunofern.filesigner.signablefile;

import pt.nunofern.filesigner.signedfile.impl.SignedFile;

public interface ISignableFile {

    SignedFile sign();

}
