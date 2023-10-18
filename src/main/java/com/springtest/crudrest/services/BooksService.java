package com.springtest.crudrest.services;

import com.springtest.crudrest.models.Book;
import com.springtest.crudrest.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> collectBooks() {
        return booksRepository.findAll();
    }
    public Book loadByPk(Integer id) {
        return booksRepository.findById(id).orElse(null);
    }
    @Transactional
    public void saveOrUpdate(Book book) {
        booksRepository.save(book);
    }
    @Transactional
    public void delete(Integer id) {
        booksRepository.deleteById(id);
    }
}
