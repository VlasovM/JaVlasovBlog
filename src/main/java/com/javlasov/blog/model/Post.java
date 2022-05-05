package com.javlasov.blog.model;

import com.javlasov.blog.model.enums.ModerationStatus;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private int moderatorId;

    @Column(name = "time")
    @NotNull
    private LocalDateTime time;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "text")
    private String text;

    @Column(name = "view_count")
    private int viewCount;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @NotNull
    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL)
    private List<PostVotes> postVotes;

    @NotNull
    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL)
    private List<PostComments> postComments;

}

