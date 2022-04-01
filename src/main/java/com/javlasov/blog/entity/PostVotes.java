package com.javlasov.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "post_votes")
public class PostVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

//    @ManyToOne (optional = false, cascade = CascadeType.ALL)
//    @JoinColumn (name = "user_id")
//    @NotNull
//    @JsonIgnore
//    private User user;

    @Column(name = "user_id")
    private int userId;


    @Column(name = "post_id")
    private int postId;
//    @ManyToOne (optional = false, cascade = CascadeType.ALL)
//    @JoinColumn (name = "post_id")
//    @NotNull
//    @JsonIgnore
//    private Post post;

    @NotNull
    @Column(name = "time")
    private Timestamp time;

    @Column(name = "value")
    @NotNull
    private int value;
}
