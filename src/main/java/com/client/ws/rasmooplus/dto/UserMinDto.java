package com.client.ws.rasmooplus.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMinDto {
    private Long id;

    private byte[] photo;

    private String photoName;
}
