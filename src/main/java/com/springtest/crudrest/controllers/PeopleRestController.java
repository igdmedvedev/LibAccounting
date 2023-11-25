package com.springtest.crudrest.controllers;

import com.springtest.crudrest.dto.BookDto;
import com.springtest.crudrest.dto.PersonDto;
import com.springtest.crudrest.models.Book;
import com.springtest.crudrest.models.Person;
import com.springtest.crudrest.security.JwtUtil;
import com.springtest.crudrest.services.PeopleService;
import com.springtest.crudrest.utils.CreateOrUpdateException;
import com.springtest.crudrest.utils.ErrorResponse;
import com.springtest.crudrest.utils.NotFoundException;
import com.springtest.crudrest.validators.PersonDtoValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PeopleRestController {
    final private PeopleService peopleService;
    final private PersonDtoValidator personValidator;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    @Autowired
    public PeopleRestController(PeopleService peopleService, PersonDtoValidator personValidator,
                                ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }
    @GetMapping
    public List<PersonDto> collectPeople() {
        List<PersonDto> people = new ArrayList<>();
        for (Person person : peopleService.collectPeople()) {
            people.add(modelMapper.map(person, PersonDto.class));
        }
        return people;
    }

    @GetMapping("/{id}")
    public PersonDto getPerson(@PathVariable("id") int id) {
        return modelMapper.map(peopleService.loadByPk(id), PersonDto.class);
    }
    @GetMapping("/{id}/books")
    public List<BookDto> getPersonBooks(@PathVariable("id") int id) {
        List<BookDto> books = new ArrayList<>();
        for (Book book : peopleService.loadByPk(id).getBooks()) {
            books.add(modelMapper.map(book, BookDto.class));
        }
        return books;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") int id) {
        //We delete the entity and not by id, so that an NotFoundException is thrown
        peopleService.delete(peopleService.loadByPk(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> editOrCreate(@RequestBody @Valid PersonDto personDto, BindingResult bindingResult) {
        personValidator.validate(personDto, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            for (FieldError e : bindingResult.getFieldErrors()) {
                builder.append(e.getField()).append(" - ").append(e.getDefaultMessage()).append(';');
            }
            throw new CreateOrUpdateException(builder.toString());
        }
        peopleService.saveOrUpdate(modelMapper.map(personDto, Person.class));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(CreateOrUpdateException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}