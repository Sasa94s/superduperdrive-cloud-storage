package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.dtos.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public boolean isCredentialIdFound(int credentialId, int userId) {
        return credentialMapper.checkCredential(credentialId, userId) == 1;
    }

    public List<CredentialDTO> getCredentials(int userId) {
        return credentialMapper.getCredentials(userId).stream()
                .map(credential -> {
                    String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());

                    return new CredentialDTO(
                            credential.getCredentialId(),
                            credential.getUrl(),
                            credential.getUsername(),
                            credential.getKey(),
                            credential.getPassword(),
                            credential.getUserId(),
                            decryptedPassword
                    );
                })
                .collect(Collectors.toList());
    }

    public int createCredential(Credential credential) throws NoRowsAffectedException {
        credential.setKey(encryptionService.generateEncodedKey());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);
        int rows = credentialMapper.createCredential(credential);
        if (rows < 1) {
            throw new NoRowsAffectedException(String.format("%d credential(s) created", rows));
        }

        return credential.getCredentialId();
    }

    public int updateCredential(Credential credential) throws NoRowsAffectedException, ArgNotFoundException {
        if (!isCredentialIdFound(credential.getCredentialId(), credential.getUserId())) {
            throw new ArgNotFoundException(String.format("%s credential not found", credential.getCredentialId()));
        }

        Credential storedCredential = credentialMapper.getCredential(credential.getCredentialId(), credential.getUserId());
        if (!credential.getPassword().equals(storedCredential.getPassword())) {
            credential.setKey(encryptionService.generateEncodedKey());
            String newPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
            credential.setPassword(newPassword);
        }
        int rows = credentialMapper.editCredential(credential);
        if (rows < 1) {
            throw new NoRowsAffectedException(String.format("%d credential(s) modified", rows));
        }

        return rows;
    }

    public void deleteCredential(int credentialId, int userId) throws ArgNotFoundException, NoRowsAffectedException {
        if (!isCredentialIdFound(credentialId, userId)) {
            throw new ArgNotFoundException(String.format("%s credential not found", credentialId));
        }

        if (!credentialMapper.deleteCredential(credentialId, userId)) {
            throw new NoRowsAffectedException("Failed to delete credential");
        }
    }
}
