package com.springtest.crudrest.controllers;

import com.springtest.crudrest.dto.BookDto;
import com.springtest.crudrest.models.Book;
import com.springtest.crudrest.models.Person;
import com.springtest.crudrest.security.JwtUtil;
import com.springtest.crudrest.services.BooksService;
import com.springtest.crudrest.services.PeopleService;
import com.springtest.crudrest.utils.CreateOrUpdateException;
import com.springtest.crudrest.utils.ErrorResponse;
import com.springtest.crudrest.utils.NotFoundException;
import com.springtest.crudrest.validators.BookDtoValidator;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/api/books")
public class BooksRestController {
    final private BooksService booksService;
    final private BookDtoValidator bookValidator;
    private final ModelMapper modelMapper;
    private final PeopleService peopleService;
    private final JwtUtil jwtUtil;


    @Autowired
    public BooksRestController(BooksService booksService, BookDtoValidator bookValidator,
                               ModelMapper modelMapper, PeopleService peopleService, JwtUtil jwtUtil) {
        this.booksService = booksService;
        this.bookValidator = bookValidator;
        this.modelMapper = modelMapper;
        this.peopleService = peopleService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<BookDto> collectBooks() {
        List<BookDto> books = new ArrayList<>();
        for (Book book : booksService.collectBooks()) {
            books.add(modelMapper.map(book, BookDto.class));
        }
        return books;
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable("id") int id) {
        return modelMapper.map(booksService.loadByPk(id), BookDto.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") int id) {
        //We delete the entity and not by id, so that an NotFoundException is thrown
        booksService.delete(booksService.loadByPk(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> editOrCreate(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        bookValidator.validate(bookDto, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            for (FieldError e : bindingResult.getFieldErrors()) {
                builder.append(e.getField()).append(" - ").append(e.getDefaultMessage()).append(';');
            }
            throw new CreateOrUpdateException(builder.toString());
        }
        booksService.saveOrUpdate(modelMapper.map(bookDto, Book.class));
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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(EntityNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}