package com.springtest.crudrest.services;

import com.springtest.crudrest.models.Librarian;
import com.springtest.crudrest.repositories.LibrarianRepository;
import com.springtest.crudrest.security.LibrarianDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LibrarianDetailService implements UserDetailsService {
    private final LibrarianRepository librarianRepository;
    @Autowired
    public LibrarianDetailService(LibrarianRepository librarianRepository) {
        this.librarianRepository = librarianRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Librarian> librarian = librarianRepository.findByUsername(username);

        if (librarian.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new LibrarianDetails(librarian.get());
    }
}
