package com.springtest.crudrest.controllers;

import com.springtest.crudrest.models.Librarian;
import com.springtest.crudrest.services.LibrariansService;
import com.springtest.crudrest.validators.LibrarianValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final LibrarianValidator librarianValidator;
    private final LibrariansService librariansService;
    @Autowired
    public AuthController(LibrarianValidator librarianValidator, LibrariansService librariansService) {
        this.librarianValidator = librarianValidator;
        this.librariansService = librariansService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("librarian") Librarian librarian) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("librarian") @Valid Librarian librarian,
                                      BindingResult bindingResult) {
        librarianValidator.validate(librarian, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        librariansService.save(librarian);
        return "redirect:/auth/login";
    }
}
