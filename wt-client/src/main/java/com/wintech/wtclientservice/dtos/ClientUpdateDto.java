package com.wintech.wtclientservice.dtos;

import com.wintech.wtclientservice.domains.enums.TypeCompanyOrPerson;
import com.wintech.wtclientservice.domains.enums.TypeRegister;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateDto {

    @NotEmpty
    private TypeCompanyOrPerson companyOrPerson;
    @NotEmpty
    private List<TypeRegister> type = new ArrayList<>();
    @NotEmpty
    private String name;
    @NotEmpty
    private String telephone1;
    private String telephone2;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String cpfOrCnpj;
    @NotEmpty
    private String rgOrIe;
    @NotEmpty
    private AddressUpdateDto address;
    private AddressUpdateDto deliveryAddress;

}
