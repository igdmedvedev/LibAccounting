package com.springtest.crudrest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class BookDto {
    private Integer id;

    @Size(min = 2, max = 50, message = "Book name should be between 2 and 50 characters")
    private String name;

    @Size(min = 2, max = 50, message = "Author name should be between 2 and 50 characters")
    private String authorName;

    @Min(value = -3000, message = "Year of writing should not be before than 3000 BC")
    @Max(value = 3000, message = "Year of writing should not be after than 3000 BC")
    private Integer year;

    private Integer personId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPersonId() {
        return personId;
    }
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
}