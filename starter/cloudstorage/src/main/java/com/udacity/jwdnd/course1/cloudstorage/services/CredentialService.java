package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.ICredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CredentialService {
    private final ICredentialMapper credentialMapper;

    public void add(Credential credential) {
        credentialMapper.insert(credential);
    }

    public boolean deleteById(int id) {
        return credentialMapper.deleteById(id) == 1;
    }

    public Credential get(int id) {
        return credentialMapper.findById(id);
    }

    public List<Credential> getAllByUser(int userId) {
        return credentialMapper.getAllByUserId(userId);
    }

    public void upsert(Credential credential) {
        if (Objects.isNull(credential.getCredentialId())) {
            credentialMapper.insert(credential);
        } else {
            credentialMapper.update(credential);
        }
    }
}
