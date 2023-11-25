package com.springtest.crudrest.services;

import com.springtest.crudrest.models.Book;
import com.springtest.crudrest.repositories.BooksRepository;
import com.springtest.crudrest.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> collectBooks(Integer page) {
        return booksRepository.findAll(PageRequest.of(page, 6, Sort.by("authorName"))).getContent();
    }
    public List<Book> collectBooks() {
        return booksRepository.findAll();
    }
    public Book loadByPk(Integer id) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isEmpty()) {
            throw new NotFoundException(String.format("Book with id = %d wasn't found.", id));
        }
        return book.get();
    }
    @Transactional
    public void saveOrUpdate(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void delete(Integer id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void delete(Book book) {
        booksRepository.delete(book);
    }
}
