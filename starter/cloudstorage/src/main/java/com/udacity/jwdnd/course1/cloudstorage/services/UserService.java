package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.IUserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.security.SecureRandom;
import java.util.Base64;

@AllArgsConstructor
@Service
public class UserService {
    private final IUserMapper userMapper;
    private final HashService hashService;

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUserByUsername(username) == null;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insert(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(),
                user.getLastName()));
    }

    public User getUser(String username) {
        return userMapper.getUserByUsername(username);
    }

    public int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (StringUtils.isEmpty(username)) {
            return 0;
        }
        return userMapper.getUserByUsername(username).getUserId();
    }
}
