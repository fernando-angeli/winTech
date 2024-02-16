package com.wintech.wtclientservice.services.validation;

import com.wintech.wtclientservice.controllers.exceptions.FieldMessage;
import com.wintech.wtclientservice.domains.Client;
import com.wintech.wtclientservice.domains.enums.TypeCompanyOrPerson;
import com.wintech.wtclientservice.dtos.ClientDto;
import com.wintech.wtclientservice.repositories.ClientRepository;
import com.wintech.wtclientservice.util.ValidateCnpj;
import com.wintech.wtclientservice.util.ValidateCpf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ClientInsertValidator implements ConstraintValidator<ClientInsertValid, ClientDto> {

    @Autowired
    private ClientRepository repository;

    @Override
    public void initialize(ClientInsertValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(ClientDto clientDto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        Client client = repository.findByEmail(clientDto.getEmail());

        if(client != null){
            list.add(new FieldMessage("E-mail", "E-mail já cadastrado."));
        }
        if(clientDto.getType().equals(TypeCompanyOrPerson.FISICA) && !ValidateCpf.validateCPF(clientDto.getCpfOrCnpj())){
            list.add(new FieldMessage("CPF", "CPF inválido."));
        }
        if(clientDto.getType().equals(TypeCompanyOrPerson.JURIDICA) && !ValidateCnpj.validateCnpj(clientDto.getCpfOrCnpj())){
            list.add(new FieldMessage("CNPJ", "CNPJ inválido."));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}
