package com.ecom.profile_service.dto;

import com.ecom.profile_service.domain.GENDER;
import com.ecom.profile_service.entity.Address;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EcomUserUpdateRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobile;

    private String about;

    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private GENDER gender;

    private List<Address> addresses;  // Can have IDs to determine update vs add
}

