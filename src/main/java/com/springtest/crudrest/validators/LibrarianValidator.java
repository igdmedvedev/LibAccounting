package com.springtest.crudrest.validators;

import com.springtest.crudrest.models.Librarian;
import com.springtest.crudrest.services.LibrariansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class LibrarianValidator implements Validator {
    private final LibrariansService librariansService;
    @Autowired
    public LibrarianValidator(LibrariansService librariansService) {
        this.librariansService = librariansService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Librarian.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Librarian librarian = (Librarian)target;
        if (librariansService.loadByUsername(librarian.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "User with the same name already exist");
        }

        String pwd = librarian.getPassword();
        if (pwd.length() < 3) {
            errors.rejectValue("password", "", "Password must contain more then 2 characters");
        } else if (pwd.length() > 30) {
            errors.rejectValue("password", "", "Password must contain less then 30 characters");
        }

        if (!Pattern.compile("[A-Z]").matcher(pwd).find()) {
            errors.rejectValue("password", "", "Password must contain uppercase letters");
        }
        if (!Pattern.compile("[a-z]").matcher(pwd).find()) {
            errors.rejectValue("password", "", "Password must contain lowercase letters");
        }
        if (!Pattern.compile("\\d").matcher(pwd).find()) {
            errors.rejectValue("password", "", "Password must contain numbers");
        }
        if (!Pattern.compile("[@№#;:%^&?.$*()=+/\\-]").matcher(pwd).find()) {
            errors.rejectValue("password", "", "Password must contain special characters: @№#;:%^&?.$*()=+/-");
        }
    }
}
