package com.damnteam.letmechat.data.dto;

import com.damnteam.letmechat.data.model.Channel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelDTO {
    private Long id;
    private String name;
    private String owner;

    public ChannelDTO(Channel channel) {
        this.id = channel.getId();
        this.name = channel.getName();
        this.owner = channel.getOwner().getName();
    }
}
