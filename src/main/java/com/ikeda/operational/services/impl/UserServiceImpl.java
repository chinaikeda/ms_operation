package com.ikeda.operational.services.impl;

import com.ikeda.operational.models.UserModel;
import com.ikeda.operational.repositories.UserRepository;
import com.ikeda.operational.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        userRepository.deleteById(userId);
    }
}
