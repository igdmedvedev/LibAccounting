package com.springtest.crudrest.validators;

import com.springtest.crudrest.dto.BookDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookDtoValidator implements Validator {
    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return BookDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        BookDto book = (BookDto)target;
        if (book.getYear() == 0) {
            errors.rejectValue("year", "", "Zero year does not exist");
        }
    }
}
