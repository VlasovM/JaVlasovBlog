package com.javlasov.blog.model;

import com.javlasov.blog.model.enums.Role;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @NotNull
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
    private int moderator;

    @Column(name = "photo")
    private String photo;

    @NotNull
    @Column(name = "reg_time")
    private LocalDateTime regTime;

    @NotNull
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private Collection<PostVotes> postVotes;

    @NotNull
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private Collection<PostComments> postComments;

    public Role getRole() {
        return moderator == 1 ? Role.MODERATOR : Role.USER;
    }

}
