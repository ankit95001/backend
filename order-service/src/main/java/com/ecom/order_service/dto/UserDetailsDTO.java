package com.ecom.order_service.dto;

import com.ecom.order_service.domain.GENDER;
import com.ecom.order_service.domain.USER_ROLE;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    private String fullName;
    private String email;
    private String mobile;
    private String image;
    private String about;
    private LocalDate dateOfBirth;
    private GENDER gender;
    private USER_ROLE role;
    private List<AddressDTO> addresses;
}

