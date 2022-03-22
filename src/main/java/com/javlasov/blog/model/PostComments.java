package com.javlasov.blog.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "post_comments")
public class PostComments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int parent_id;

    @NotNull
    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "post_id")
    private Post post;

    @NotNull
    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id")
    private User user;

    @NotNull
    private Timestamp time;

    @NotNull
    private String text;
}
