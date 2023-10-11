package com.springtest.crudrest.controllers;

import com.springtest.crudrest.dao.BooksDao;
import com.springtest.crudrest.dao.PeopleDao;
import com.springtest.crudrest.models.Book;
import com.springtest.crudrest.models.Person;
import com.springtest.crudrest.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {
    final private PeopleDao peopleDao;
    final private PersonValidator personValidator;
    final private BooksDao booksDao;
    @Autowired
    public PeopleController(PeopleDao peopleDao, PersonValidator personValidator, BooksDao booksDao) {
        this.peopleDao = peopleDao;
        this.personValidator = personValidator;
        this.booksDao = booksDao;
    }
    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("people", peopleDao.collectPeople());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String showPersonPage(Model model, @PathVariable("id") int id) {
        Person person = peopleDao.loadByPk(id);
        if (person == null) {
            model.addAttribute("msg", "Reader not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("person", person);
        return "people/personPage";
    }
    @GetMapping("/{id}/books")
    public String showPersonBooks(Model model, @PathVariable("id") int id) {
        Person person = peopleDao.loadByPk(id);
        if (person == null) {
            model.addAttribute("msg", "Reader not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("books", booksDao.collectBooksByPerson(id));
        model.addAttribute("title", "Book List by " + person.getFullName());
        return "books/index";
    }

    @GetMapping("/new")
    public String createPersonForm(@ModelAttribute("person") Person person) {
        return "people/personForm";
    }

    @GetMapping("/{id}/edit")
    public String changePersonForm(Model model, @PathVariable("id") int id) {
        Person person = peopleDao.loadByPk(id);
        if (person == null) {
            model.addAttribute("msg", "Reader not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("person", person);
        return "people/personForm";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id){
        peopleDao.delete(id);
        return "redirect:/people";
    }

    @PatchMapping
    public String edit(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/personForm";
        }
        peopleDao.update(person);
        return "redirect:/people/" + person.getId(); //нужно избавиться от конкатинации
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/personForm";
        }
        peopleDao.create(person);
        return "redirect:/people/";
    }
}
