package com.springtest.crudrest.controllers;

import com.springtest.crudrest.models.Book;
import com.springtest.crudrest.services.BooksService;
import com.springtest.crudrest.services.PeopleService;
import com.springtest.crudrest.utils.NotFoundException;
import com.springtest.crudrest.validators.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {
    final private BooksService booksService;
    final private PeopleService peopleService;
    BookValidator bookValidator;

    @Autowired
    public BooksController(BooksService booksService, BookValidator bookValidator, PeopleService peopleService) {
        this.booksService = booksService;
        this.bookValidator = bookValidator;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String mainPage(Model model, @RequestParam(required=false, name="page") Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        List<Book> books = booksService.collectBooks(page - 1);
        List<Book> booksOnNextPage = booksService.collectBooks(page);

        model.addAttribute("books", books);
        model.addAttribute("title", "Book List");
        model.addAttribute("prevPage", page - 1);
        model.addAttribute("nextPage", booksOnNextPage == null || booksOnNextPage.isEmpty() ? 0 : page + 1);
        model.addAttribute("isAdmin", userIsAdmin());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String showBookPage(Model model, @PathVariable("id") int id) {
        Book book = booksService.loadByPk(id);
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
                       @RequestParam(required=false, name="personId") Integer personId) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/bookForm";
        }
        if (personId != null) {
            book.setPerson(peopleService.loadByPk(personId));
        }
        booksService.saveOrUpdate(book);
        return "redirect:/books/" + book.getId();
        //TODO: наверное стоит избавиться от конкатенации
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @RequestParam(required=false, name="personId") Integer personId) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/bookForm";
        }
        if (personId != null) {
            book.setPerson(peopleService.loadByPk(personId));
        }
        booksService.saveOrUpdate(book);
        return "redirect:/books";
    }

    private boolean userHasRole(String role) {
        List<? extends GrantedAuthority> authority = (List<? extends GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authority != null && !authority.isEmpty() && authority.get(0).getAuthority().equals(role);
    }

    private boolean userIsAdmin() {
        return userHasRole("ROLE_ADMIN");
    }

    @ExceptionHandler
    private String handleException(NotFoundException ex, Model model) {
        model.addAttribute("msg", ex.getMessage() + "The link may be invalid.");
        return "notFoundPage";
    }
}
