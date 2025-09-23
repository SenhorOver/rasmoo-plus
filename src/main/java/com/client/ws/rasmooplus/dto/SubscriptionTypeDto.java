package com.client.ws.rasmooplus.dto;

import com.client.ws.rasmooplus.model.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionTypeDto {
    private Long id;
    private String name;
    private Integer accessMonths;
    private BigDecimal price;
    private String productKey;

    public SubscriptionTypeDto(SubscriptionType entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.accessMonths = entity.getAccessMonths();
        this.price = entity.getPrice();
        this.productKey = entity.getProductKey();
    }
}
