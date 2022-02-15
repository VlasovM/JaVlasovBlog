package main.model;

import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private boolean is_active;

    @NotNull
    private ModerationStatus moderation_status;

    private int moderator_id;

    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id")
    @NotNull
    private User user;

    @NotNull
    private Timestamp time;

    @NotNull
    private String title;

    @NotNull
    private String text;

    private int view_count;

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

