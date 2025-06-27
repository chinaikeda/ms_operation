package com.ikeda.operational.consumers;

import com.ikeda.operational.dtos.UserEventRecordDto;
import com.ikeda.operational.enums.ActionType;
import com.ikeda.operational.services.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    final UserService userService;

    public UserConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ikeda.broker.queue.userEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ikeda.broker.exchange.userEventExchange}",
                    type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true"))
    )
    public void listenUserEvent(@Payload UserEventRecordDto userEventRecordDto){
        var userModel = userEventRecordDto.converToUserModel();

        switch (ActionType.valueOf(userEventRecordDto.actionType())){
            case CREATE, UPDATE -> userService.save(userModel);
            case DELETE -> userService.delete(userEventRecordDto.userId());
        }
    }

}
