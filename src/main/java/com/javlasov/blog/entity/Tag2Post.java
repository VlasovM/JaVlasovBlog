package com.javlasov.blog.entity;

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

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    @NotNull
    private Post post;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    @NotNull
    private Tag tag;
}
