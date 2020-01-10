package com.damnteam.letmechat.data.dto;

import com.damnteam.letmechat.data.model.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericMessage {

    Long id;

    String username;

    String message;

    String time;

    public GenericMessage(Message persistedMessage) {
        this.id = persistedMessage.getId();
        this.username = persistedMessage.getUser().getName();
        this.message = persistedMessage.getMessage();
        this.time = persistedMessage.getTime().toString();
    }
}
