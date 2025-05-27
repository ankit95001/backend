package com.ecom.order_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}

