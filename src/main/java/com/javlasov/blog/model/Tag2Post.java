package com.javlasov.blog.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tag2post")
public class Tag2Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "post_id")
    private Post post;

    @NotNull
    @ManyToOne (optional = false, cascade = CascadeType.ALL)
    @JoinColumn (name = "tag_id")
    private Tags tag;
}
