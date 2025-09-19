package com.client.ws.rasmooplus.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "users_id")
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String cpf;

    @Column(name = "dt_subscription")
    private LocalDate dtSubscription = LocalDate.now();
    @Column(name = "dt_expiration")
    private LocalDate dtExpiration;

    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @ManyToOne
    @JoinColumn(name = "subscriptions_type_id")
    private SubscriptionsType subscriptionsType;

    public User(Long id, String name, String email, String phone, String cpf, LocalDate dtSubscription, LocalDate dtExpiration, UserType userType, SubscriptionsType subscriptionsType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpf = cpf;
        this.dtSubscription = dtSubscription;
        this.dtExpiration = dtExpiration;
        this.userType = userType;
        this.subscriptionsType = subscriptionsType;
    }

    public User() {
    }
}
