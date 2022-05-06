package pt.nunofern.filesigner.signablefile.signer;

import pt.nunofern.filesigner.exceptions.SignFileException;

public interface IFileSigner<T> {

    byte[] sign(T toSign) throws SignFileException;
}
