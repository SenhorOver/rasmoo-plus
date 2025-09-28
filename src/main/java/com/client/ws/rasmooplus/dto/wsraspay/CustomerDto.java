package com.client.ws.rasmooplus.dto.wsraspay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private String id;
    private String cpf;
    private String email;
    // @JsonProperty("first_name") -> Caso o JSON fosse em snake case
    private String firstName;
    private String lastName;
}
