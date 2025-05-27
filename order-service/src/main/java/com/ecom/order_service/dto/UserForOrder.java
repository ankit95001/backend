package com.ecom.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForOrder {
    private String fullName;
    private String email;
    private String mobile;
    private List<AddressDTO> addresses;
}
