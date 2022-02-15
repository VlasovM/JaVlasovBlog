package main.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table (name = "global_settings")
public class GlobalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private String value;
}
