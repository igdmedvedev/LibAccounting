package com.springtest.crudrest.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Book {
    private Integer id;
    @Size(min = 2, max = 50, message = "Book name should be between 2 and 50 characters")
    private String name;
    private Integer personId;
    @Size(min = 2, max = 50, message = "Author name should be between 2 and 50 characters")
    private String authorName;
    @Min(value = -3000, message = "Year of writing should not be before than 3000 BC")
    @Max(value = 3000, message = "Year of writing should not be after than 3000 BC")
    private Integer year;

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

    public Integer getPersonId() {
        return personId;
    }
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
    public void setPersonId(Person person) {
        this.personId = person.getId();
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

    public boolean getTrue() {return true;}
}
