package main.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

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


    private int user_id;

    @NotNull
    private Timestamp time;

    @NotNull
    private String title;

    @NotNull
    private String text;

    private int view_count;



}

