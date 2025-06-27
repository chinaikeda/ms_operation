package com.ikeda.operational.dtos;

import com.ikeda.operational.models.UserModel;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public record UserEventRecordDto(UUID userId,
                                 String login,
                                 String email,
                                 String name,
                                 String userStatus,
                                 String userType,
                                 String phoneNumber,
                                 String imageUrl,
                                 String actionType) {

    public UserModel converToUserModel(){
        var userModel = new UserModel();
        BeanUtils.copyProperties(this, userModel);
        return userModel;
    }
}
