package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public boolean isCredentialIdFound(int credentialId) {
        return credentialMapper.checkCredential(credentialId) == 1;
    }

    public List<Credential> getCredentials() {
        return credentialMapper.getCredentials();
    }

    public int createCredential(Credential credential) throws NoRowsAffectedException {
        int rows = credentialMapper.createCredential(credential);
        if (rows < 1) {
            throw new NoRowsAffectedException(String.format("%d credential(s) created", rows));
        }

        return credential.getCredentialId();
    }

    public int updateCredential(Credential credential) throws NoRowsAffectedException, ArgNotFoundException {
        if (!isCredentialIdFound(credential.getCredentialId())) {
            throw new ArgNotFoundException(String.format("%s credential not found", credential.getCredentialId()));
        }

        int rows = credentialMapper.editCredential(credential);
        if (rows < 1) {
            throw new NoRowsAffectedException(String.format("%d credential(s) modified", rows));
        }

        return rows;
    }

    public void deleteCredential(int credentialId) throws ArgNotFoundException, NoRowsAffectedException {
        if (!isCredentialIdFound(credentialId)) {
            throw new ArgNotFoundException(String.format("%s credential not found", credentialId));
        }

        if (!credentialMapper.deleteCredential(credentialId)) {
            throw new NoRowsAffectedException("Failed to delete credential");
        }
    }
}
