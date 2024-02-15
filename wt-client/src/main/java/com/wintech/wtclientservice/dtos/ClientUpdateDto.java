package com.wintech.wtclientservice.dtos;

import com.wintech.wtclientservice.domains.enums.TypeCompanyOrPerson;
import com.wintech.wtclientservice.domains.enums.TypeRegister;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateDto {
    
    private TypeCompanyOrPerson companyOrPerson;
    private List<TypeRegister> type = new ArrayList<>();
    private String name;
    private String telephone1;
    private String telephone2;
    private String email;
    private String cpfOrCnpj;
    private String rgOrIe;
    private AddressUpdateDto address;
    private AddressUpdateDto deliveryAddress;

}
