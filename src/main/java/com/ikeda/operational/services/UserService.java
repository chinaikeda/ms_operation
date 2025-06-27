package com.ikeda.operational.services;

import com.ikeda.operational.models.UserModel;

import java.util.UUID;

public interface UserService {
    UserModel save(UserModel userModel);

    void delete(UUID userId);
}
