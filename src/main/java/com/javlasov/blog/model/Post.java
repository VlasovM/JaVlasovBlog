package com.javlasov.blog.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Data
@Table(name = "posts")
@NoArgsConstructor
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "is_active")
    private short active;

    @NotNull
    @Column(name = "moderation_status")
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private int moderatorId;

    @NotNull
    @Column(name = "time")
    private LocalDate time;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "text")
    private String text;

    @Column(name = "view_count")
    private int viewCount;

    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id")
    @NotNull
    private User user;

    @NotNull
    @ManyToMany
    @JoinTable (name = "tag2post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Collection<Tags> tags;

    @NotNull
    @OneToMany(mappedBy = "post")
    private Collection<PostVotes> postVotes;

    @NotNull
    @OneToMany (mappedBy = "post")
    private Collection<PostComments> postComments;
}

