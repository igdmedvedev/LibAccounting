package com.springtest.crudrest.controllers;

import com.springtest.crudrest.dto.AuthenticationDto;
import com.springtest.crudrest.models.Librarian;
import com.springtest.crudrest.models.Person;
import com.springtest.crudrest.security.AuthProviderImpl;
import com.springtest.crudrest.security.JwtUtil;
import com.springtest.crudrest.services.LibrariansService;
import com.springtest.crudrest.validators.LibrarianValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final LibrarianValidator librarianValidator;
    private final LibrariansService librariansService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(LibrarianValidator librarianValidator, LibrariansService librariansService, JwtUtil jwtUtil, AuthProviderImpl authProviderImpl) {
        this.librarianValidator = librarianValidator;
        this.librariansService = librariansService;
        this.jwtUtil = jwtUtil;
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

    @GetMapping("/jwt")
    public String generateJwtForWeb (Model model) {
        model.addAttribute("jwt", jwtUtil.generateToken(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "auth/jwtTokenPage";
    }
}
