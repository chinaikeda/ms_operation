package com.ikeda.operational.services;

import com.ikeda.operational.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserModel> findById(UUID userId);

    void delete(UUID userId);

    UserModel save(UserModel userModel);

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);
}
