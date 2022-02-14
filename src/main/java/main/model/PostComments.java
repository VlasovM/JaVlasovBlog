package main.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class PostComments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int parent_id;

    @NotNull
    private int post_id;

    @NotNull
    private int user_id;

    @NotNull
    private Timestamp time;

    @NotNull
    private String text;
}
