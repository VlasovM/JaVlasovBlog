package main.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Data
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;

    private String code;

    private boolean is_moderator;

    @NotNull
    private Timestamp reg_time;

    private String text;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @NotNull
    private Collection<Post> posts;

    @NotNull
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<PostVotes> postVotes;

    @NotNull
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<PostComments> postComments;
}
