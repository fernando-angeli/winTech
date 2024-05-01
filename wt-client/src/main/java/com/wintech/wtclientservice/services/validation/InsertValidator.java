package com.wintech.wtclientservice.services.validation;

import com.wintech.wtclientservice.controllers.exceptions.FieldMessage;
import com.wintech.wtclientservice.domains.Client;
import com.wintech.wtclientservice.domains.enums.TypeCompanyOrPerson;
import com.wintech.wtclientservice.dtos.ClientDto;
import com.wintech.wtclientservice.repositories.ClientRepository;
import com.wintech.wtclientservice.utilities.CNPJValidator;
import com.wintech.wtclientservice.utilities.CPFValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class InsertValidator implements ConstraintValidator<InsertValid, ClientDto> {

    @Autowired
    private ClientRepository repository;

    @Override
    public void initialize(InsertValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(ClientDto clientDto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        Client client = repository.findByEmail(clientDto.getEmail());

        if(client != null){
            list.add(new FieldMessage("email", "E-mail já cadastrado."));
        }
        if((clientDto.getCompanyOrPerson().equals(TypeCompanyOrPerson.FISICA)) && !(CPFValidator.validarCPF(clientDto.getCpfOrCnpj()))){
            list.add(new FieldMessage("cpfOrCnpj", "CPF inválido."));
        }
        if(clientDto.getCompanyOrPerson().equals(TypeCompanyOrPerson.JURIDICA) && !CNPJValidator.validarCNPJ(clientDto.getCpfOrCnpj())){
            list.add(new FieldMessage("cpfOrCnpj", "CNPJ inválido."));
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
