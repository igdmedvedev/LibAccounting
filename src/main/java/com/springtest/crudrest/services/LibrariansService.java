package com.springtest.crudrest.services;

import com.springtest.crudrest.models.Librarian;
import com.springtest.crudrest.repositories.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LibrariansService {
    private final LibrarianRepository librarianRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LibrariansService(LibrarianRepository librarianRepository, PasswordEncoder passwordEncoder) {
        this.librarianRepository = librarianRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Librarian> loadByUsername(String username) {
        return librarianRepository.findByUsername(username);
    }

    @Transactional
    public void save(Librarian librarian) {
        librarian.setRole("ROLE_USER");

        String encodedPassword = passwordEncoder.encode(librarian.getPassword());
        librarian.setPassword(encodedPassword);

        librarianRepository.save(librarian);
    }
}
