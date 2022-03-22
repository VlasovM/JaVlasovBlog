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

    @NotNull
    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id")
    private User user;

    @NotNull
    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "post_id")
    private Post post;

    @NotNull
    private Timestamp time;

    private boolean value;
}
