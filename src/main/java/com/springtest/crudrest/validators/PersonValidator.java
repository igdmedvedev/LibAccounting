package com.springtest.crudrest.validators;

import com.springtest.crudrest.models.Person;
import com.springtest.crudrest.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;
    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Person person = (Person)target;
        Person findedPerson = peopleService.loadByEmail(person.getEmail());
        if (findedPerson != null && !findedPerson.getId().equals(person.getId())) {
            errors.rejectValue("email", "", "Person with this email already exists");
        }
    }
}
