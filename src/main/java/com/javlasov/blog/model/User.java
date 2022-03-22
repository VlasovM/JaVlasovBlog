package com.javlasov.blog.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Data
@Table (name = "users")
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "code")
    private String code;

    @Column(name = "is_moderator")
    private boolean is_moderator;

    @NotNull
    @Column(name = "reg_time")
    private Timestamp regTime;

    @OneToMany(mappedBy = "user")
    @NotNull
    private Collection<Post> posts;

    @NotNull
    @OneToMany(mappedBy = "user")
    private Collection<PostVotes> postVotes;

    @NotNull
    @OneToMany(mappedBy = "user")
    private Collection<PostComments> postComments;
}
