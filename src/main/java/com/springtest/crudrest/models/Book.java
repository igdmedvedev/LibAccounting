package com.springtest.crudrest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    @Size(min = 2, max = 50, message = "Book name should be between 2 and 50 characters")
    private String name;
    @Column(name = "authorname")
    @Size(min = 2, max = 50, message = "Author name should be between 2 and 50 characters")
    private String authorName;
    @Column(name = "year")
    @Min(value = -3000, message = "Year of writing should not be before than 3000 BC")
    @Max(value = 3000, message = "Year of writing should not be after than 3000 BC")
    private Integer year;
    @ManyToOne
    @JoinColumn(name = "personid", referencedColumnName = "id")
    private Person person;

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

    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }
    public Integer getPersonId() {
        return person == null ? null : person.getId();
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
}
