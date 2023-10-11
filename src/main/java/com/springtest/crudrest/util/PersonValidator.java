package com.springtest.crudrest.util;

import com.springtest.crudrest.dao.PeopleDao;
import com.springtest.crudrest.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PeopleDao peopleDao;
    @Autowired
    public PersonValidator(PeopleDao peopleDao) {
        this.peopleDao = peopleDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person)target;
        Person findedPerson = peopleDao.loadByEmail(person.getEmail());
        if (findedPerson != null && !findedPerson.getId().equals(person.getId())) {
            errors.rejectValue("email", "", "Person with this email already exists");
        }
    }
}
