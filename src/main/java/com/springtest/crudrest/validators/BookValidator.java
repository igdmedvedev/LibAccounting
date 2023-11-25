package com.springtest.crudrest.validators;

import com.springtest.crudrest.models.Book;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Book book = (Book)target;
        if (book.getYear() == 0) {
            errors.rejectValue("year", "", "Zero year does not exist");
        }
    }
}
