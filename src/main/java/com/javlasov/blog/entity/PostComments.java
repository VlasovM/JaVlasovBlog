package com.javlasov.blog.entity;

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
    @Column(name = "id")
    private int id;

    @Column(name = "parent_id")
    private Integer parentId;

    @NotNull
    @Column(name = "time")
    private Timestamp time;

    @NotNull
    @Column(name = "text")
    private String text;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "post_id")
    private int postId;
//
//    @NotNull
//    @ManyToOne (optional = false, cascade = CascadeType.ALL)
//    @JoinColumn (name = "post_id")
//    private Post post;
//
//    @NotNull
//    @ManyToOne (optional = false, cascade = CascadeType.ALL)
//    @JoinColumn (name = "user_id")
//    private User user;
}