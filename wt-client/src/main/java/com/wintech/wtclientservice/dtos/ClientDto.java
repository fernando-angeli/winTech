package com.wintech.wtclientservice.dtos;

import com.wintech.wtclientservice.domains.enums.TypeCompanyOrPerson;
import com.wintech.wtclientservice.domains.enums.TypeRegister;
import com.wintech.wtclientservice.services.validation.InsertValid;
import jakarta.persistence.Column;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@InsertValid
public class ClientDto {

    private Long id;

    @NotNull(message = "Informe se o cadastro é do tipo PF ou PJ.")
    private TypeCompanyOrPerson companyOrPerson;

    @NotNull(message = "Informe o tipo de cadastro.")
    private List<TypeRegister> type = new ArrayList<>();

    @NotBlank(message = "Nome é obrigatório.")
    private String name;

    @NotBlank(message = "Telefone é obrigatório.")
    private String telephone1;

    private String telephone2;

    @NotBlank(message = "E-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    private String email;

    @NotEmpty(message = "Informe o número do documento.")
    private String cpfOrCnpj;

    @NotEmpty(message = "Informe o número do documento.")
    private String rgOrIe;

    @NotNull(message = "Informe um endereço.")
    private AddressDto address;

    private AddressDto deliveryAddress;

}
