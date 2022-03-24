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

    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id")
    @NotNull
    private User user;

    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "post_id")
    @NotNull
    private Post post;

    @NotNull
    @Column(name = "time")
    private Timestamp time;

    @Column(name = "value")
    @NotNull
    private boolean value;
}
