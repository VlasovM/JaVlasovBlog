package com.javlasov.blog.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "tags")
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    @NotNull
    @ManyToMany
    @JoinTable (name = "tag2post",
        joinColumns = @JoinColumn (name = "tag_id"),
        inverseJoinColumns = @JoinColumn (name = "post_id"))
    private Collection<Post> posts;
}
