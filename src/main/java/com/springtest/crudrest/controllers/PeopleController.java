package com.springtest.crudrest.controllers;

import com.springtest.crudrest.models.Person;
import com.springtest.crudrest.services.PeopleService;
import com.springtest.crudrest.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {
    final private PeopleService peopleService;
    final private PersonValidator personValidator;
    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }
    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("people", peopleService.collectPeople());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String showPersonPage(Model model, @PathVariable("id") int id) {
        Person person = peopleService.loadByPk(id);
        if (person == null) {
            model.addAttribute("msg", "Reader not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("person", person);
        return "people/personPage";
    }
    @GetMapping("/{id}/books")
    public String showPersonBooks(Model model, @PathVariable("id") int id) {
        Person person = peopleService.loadByPk(id, true);
        if (person == null) {
            model.addAttribute("msg", "Reader not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("books", person.getBooks());
        model.addAttribute("title", "Book List by " + person.getFullName());
        return "books/index";
    }

    @GetMapping("/new")
    public String createPersonForm(@ModelAttribute("person") Person person) {
        return "people/personForm";
    }

    @GetMapping("/{id}/edit")
    public String changePersonForm(Model model, @PathVariable("id") int id) {
        Person person = peopleService.loadByPk(id);
        if (person == null) {
            model.addAttribute("msg", "Reader not found. The link may be invalid.");
            return "notFoundPage";
        }
        model.addAttribute("person", person);
        return "people/personForm";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id){
        peopleService.delete(id);
        return "redirect:/people";
    }

    @PatchMapping
    public String edit(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/personForm";
        }
        peopleService.saveOrUpdate(person);
        return "redirect:/people/" + person.getId();
        //TODO: наверное стоит избавиться от конкатенации
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/personForm";
        }
        peopleService.saveOrUpdate(person);
        return "redirect:/people";
    }
}
