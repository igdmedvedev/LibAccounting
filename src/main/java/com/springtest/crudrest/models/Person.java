package com.springtest.crudrest.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springtest.crudrest.converters.GendersConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {
    public Person() {}

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 17, message = "First name should be between 2 and 17 characters")
    @Column(name = "firstname")
    private String firstName;
    @Size(min = 2, max = 17, message = "Last name should be between 2 and 17 characters")
    @Column(name = "lastname")
    private String lastName;
    @Size(max = 17, message = "Middle name should be no more 17 characters")
    @Column(name = "middlename")
    private String middleName;
    @NotNull(message = "Birthday should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private Date birthday;
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;
    @NotNull(message = "Gender should not be empty")
    @Column(name = "gender")
    @Convert(converter = GendersConverter.class)
    private Gender gender;

    @OneToMany(mappedBy = "person")
    @JsonManagedReference
    private List<Book> books;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public List<Book> getBooks() {
        return books;
    }
    @JsonIgnore
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public Gender getGender() {
        return gender;
    }


    public String getFormattedBirthday() {
        //Used in templates
        return new SimpleDateFormat("dd MMMM yyyy").format(birthday.getTime());
    }

    public String getFullName() {
        //Used in templates
        return lastName + " " + firstName + (middleName == null ? "" : " " + middleName);
    }
}
