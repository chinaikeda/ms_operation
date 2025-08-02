package com.ikeda.operational.services.impl;

import com.ikeda.operational.dtos.NotificationRecordCommandDto;
import com.ikeda.operational.exceptions.NotFoundException;
import com.ikeda.operational.models.UserModel;
import com.ikeda.operational.publishers.NotificationCommandPublisher;
import com.ikeda.operational.repositories.UserRepository;
import com.ikeda.operational.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LogManager.getLogger(UserServiceImpl.class);

    final UserRepository userRepository;
    final NotificationCommandPublisher notificationCommandPublisher;

    public UserServiceImpl(UserRepository userRepository, NotificationCommandPublisher notificationCommandPublisher) {
        this.userRepository = userRepository;
        this.notificationCommandPublisher = notificationCommandPublisher;
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        Optional<UserModel> userModelOptional = userRepository.findById(userId);
        if (userModelOptional.isEmpty()){
            throw new NotFoundException("Error: User not found.");
        }
        return userModelOptional;
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        logger.info(String.format("ms_operational - User delete userId {%s} - ", userId));
        userRepository.deleteById(userId);
    }

    @Override
    public UserModel save(UserModel userModel) {
        logger.info(String.format("ms_operational - User save userId {%s} - ", userModel.getUserId()));
        userRepository.save(userModel);

//        TODO - AI - Somente teste para deixar pronto, pois neste MS haverá notificação
//        try {
//            var notificationRecordCommandDto = new NotificationRecordCommandDto(
//                    "InclusãoBem vindo!!!",
//                    userModel.getName() + " seu registro foi replicado com sucesso!",
//                    userModel.getUserId());
//
//            notificationCommandPublisher.publishNotificationCommand(notificationRecordCommandDto);
//        } catch (Exception e){
//            logger.error("Error sending notification!");
//        }

        return userModel;
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }
}