package com.client.ws.rasmooplus.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionTypeDto extends RepresentationModel<SubscriptionTypeDto> implements Serializable {
    private Long id;

    @Size(min = 5, max = 15, message = "deve ter um valor entre 5 e 30 caracteres")
    private String name;

    @Max(value = 12, message = "não pode ser maior que 12")
    private Integer accessMonths;

    @NotNull(message = "não pode ser nulo")
    private BigDecimal price;

    @Size(min = 5, max = 15, message = "deve ter um valor entre 5 e 15 caracteres")
    private String productKey;
}
