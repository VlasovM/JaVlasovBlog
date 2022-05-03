package com.javlasov.blog.model;

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

    @Column(name = "user_id")
    private int userId;


    @Column(name = "post_id")
    private int postId;

    @NotNull
    @Column(name = "time")
    private Timestamp time;

    @Column(name = "value")
    @NotNull
    private int value;
}
