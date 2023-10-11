package com.springtest.crudrest.dao;

import com.springtest.crudrest.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BooksDao {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public BooksDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Book> collectBooks() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }
    public List<Book> collectBooksByPerson(Integer personId) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE personId = ?", new Object[]{personId}, new BeanPropertyRowMapper<>(Book.class));
    }

    public Book loadByPk(Integer id) {
        List<Book> book = jdbcTemplate.query("SELECT * FROM Book WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
        return book.isEmpty() ? null : book.get(0);
    }

    public void delete(Integer id) {
        if (id == null) {
            return;
        }
        jdbcTemplate.update("delete from Book where id = ?", id);
    }

    public void create(Book book) {
        jdbcTemplate.update("insert into Book (personId, name, authorName, year) values (?, ?, ?, ?)",
                book.getPersonId(), book.getName(), book.getAuthorName(), book.getYear());
    }

    public void update(Book book) {
        jdbcTemplate.update("update Book set personId = ?, name = ?, authorName = ?, year = ? where id = ?",
                book.getPersonId(), book.getName(), book.getAuthorName(), book.getYear(), book.getId());
    }
}
