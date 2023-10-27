package com.springtest.crudrest.converters;

import com.springtest.crudrest.models.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GendersConverter implements AttributeConverter<Gender, String>, org.springframework.core.convert.converter.Converter<String, Gender> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return null;
        }
        return gender.getValue();
    }

    @Override
    public Gender convertToEntityAttribute(String val) {
        return Gender.getValue(val);
    }

    @Override
    public Gender convert(String val) {
        return Gender.getValue(val);
    }
}
