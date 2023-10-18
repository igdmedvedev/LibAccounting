package com.springtest.crudrest.controllers;

import com.springtest.crudrest.models.Book;
import com.springtest.crudrest.services.BooksService;
import com.springtest.crudrest.services.PeopleService;
import com.springtest.crudrest.util.BooksValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BooksController {
    final private BooksService booksService;
    final private PeopleService peopleService;
    BooksValidator bookValidator;

    @Autowired
    public BooksController(BooksService booksService, BooksValidator bookValidator, PeopleService peopleService) {
        this.booksService = booksService;
        this.bookValidator = bookValidator;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("books", booksService.collectBooks());
        model.addAttribute("title", "Book List");
        return "books/index";
    }

    @GetMapping("/{id}")
    public String showBookPage(Model model, @PathVariable("id") int id) {
        Book book = booksService.loadByPk(id);
        if (book == null) {
            model.addAttribute("msg", "Book not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("book", book);
        model.addAttribute("personName", book.getPerson() != null ? peopleService.loadByPk(book.getPerson().getId()).getFullName() : null);
        return "books/bookPage";
    }

    @GetMapping("/new")
    public String createBookForm(Model model, @ModelAttribute("book") Book book) {
        model.addAttribute("people", peopleService.collectPeople());
        return "books/bookForm";
    }

    @GetMapping("/{id}/edit")
    public String changeBookForm(Model model, @PathVariable("id") int id) {
        Book book = booksService.loadByPk(id);
        if (book == null) {
            model.addAttribute("msg", "Book not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.collectPeople());
        return "books/bookForm";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping
    public String edit(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                       @RequestParam("personId") Integer personId) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/bookForm";
        }
        book.setPerson(peopleService.loadByPk(personId));
        booksService.saveOrUpdate(book);
        return "redirect:/books/" + book.getId();
        //TODO: наверное стоит избавиться от конкатенации
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @RequestParam("personId") Integer personId) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/bookForm";
        }
        book.setPerson(peopleService.loadByPk(personId));
        booksService.saveOrUpdate(book);
        return "redirect:/books";
    }
}
