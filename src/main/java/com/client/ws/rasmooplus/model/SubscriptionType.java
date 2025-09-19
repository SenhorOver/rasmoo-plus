package com.client.ws.rasmooplus.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "subscriptions_type")
public class SubscriptionType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscriptions_type_id")
    private Long id;

    private String name;
    @Column(name = "access_month")
    private Integer accessMonths;
    private BigDecimal price;
    @Column(name = "product_key")
    private String productKey;

    public SubscriptionType(Long id, String name, Integer accessMonths, BigDecimal price, String productKey) {
        this.id = id;
        this.name = name;
        this.accessMonths = accessMonths;
        this.price = price;
        this.productKey = productKey;
    }

    public SubscriptionType() {
    }
}
