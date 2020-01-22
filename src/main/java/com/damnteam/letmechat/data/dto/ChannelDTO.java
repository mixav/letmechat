package com.damnteam.letmechat.data.dto;

import com.damnteam.letmechat.data.model.Channel;
import com.damnteam.letmechat.data.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChannelDTO {
    private Long id;
    private String name;
    private String owner;
    private String creator;
    private int subscribersCount;

    public ChannelDTO(Channel channel) {
        this.id = channel.getId();
        this.name = channel.getName();
        this.owner = channel.getOwner().getName();
        this.creator = channel.getCreator().getName();
        this.subscribersCount = channel.getSubscribers().size();
    }

    public static ChannelDTO getForUser(Channel channel, User user) {
        var dto = new ChannelDTO();
        dto.setId(channel.getId())
                .setName(channel.getName())
                .setSubscribersCount(channel.getSubscribers().size());
        if (user.getId().equals(channel.getOwner().getId()) ||
                user.getId().equals(channel.getCreator().getId())) {
            dto.setCreator(channel.getCreator().getName())
                    .setOwner(channel.getOwner().getName());
        }
        return dto;
    }
}
