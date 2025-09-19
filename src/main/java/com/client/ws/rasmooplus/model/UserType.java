package com.client.ws.rasmooplus.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "user_type")
public class UserType implements Serializable {
    @Id
    @Column(name = "user_type_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    public UserType(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    public UserType() {}
}
