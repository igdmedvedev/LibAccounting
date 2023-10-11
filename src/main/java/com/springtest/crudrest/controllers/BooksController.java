package com.springtest.crudrest.controllers;

import com.springtest.crudrest.dao.BooksDao;
import com.springtest.crudrest.dao.PeopleDao;
import com.springtest.crudrest.models.Book;
import com.springtest.crudrest.util.BooksValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {
    final private BooksDao booksDao;
    final private PeopleDao peopleDao;
    BooksValidator bookValidator;

    @Autowired
    public BooksController(BooksDao booksDao, BooksValidator bookValidator, PeopleDao peopleDao) {
        this.booksDao = booksDao;
        this.bookValidator = bookValidator;
        this.peopleDao = peopleDao;
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("books", booksDao.collectBooks());
        model.addAttribute("title", "Book List");
        return "books/index";
    }

    @GetMapping("/{id}")
    public String showBookPage(Model model, @PathVariable("id") int id) {
        Book book = booksDao.loadByPk(id);
        if (book == null) {
            model.addAttribute("msg", "Book not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("book", book);
        model.addAttribute("personName", book.getPersonId() != null ? peopleDao.loadByPk(book.getPersonId()).getFullName() : null);
        return "books/bookPage";
    }

    @GetMapping("/new")
    public String createBookForm(Model model, @ModelAttribute("book") Book book) {
        model.addAttribute("people", peopleDao.collectPeople());
        return "books/bookForm";
    }

    @GetMapping("/{id}/edit")
    public String changeBookForm(Model model, @PathVariable("id") int id) {
        Book book = booksDao.loadByPk(id);
        if (book == null) {
            model.addAttribute("msg", "Book not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("book", book);
        model.addAttribute("people", peopleDao.collectPeople());
        return "books/bookForm";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksDao.delete(id);
        return "redirect:/books";
    }

    @PatchMapping
    public String edit(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                       @RequestParam("personId") Integer personId) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/bookForm";
        }
        book.setPersonId(personId);
        booksDao.update(book);
        return "redirect:/books/" + book.getId(); //нужно избавиться от конкатинации
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @RequestParam("personId") Integer personId) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/bookForm";
        }
        book.setPersonId(personId);
        booksDao.create(book);
        return "redirect:/books/";
    }
}
