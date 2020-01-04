package com.damnteam.letmechat.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User owner;
}
