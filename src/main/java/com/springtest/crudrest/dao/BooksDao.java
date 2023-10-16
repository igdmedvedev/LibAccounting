package com.springtest.crudrest.dao;

import com.springtest.crudrest.models.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BooksDao {
    private final SessionFactory sessionFactory;
    @Autowired
    public BooksDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Book> collectBooks() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Book", Book.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Book loadByPk(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Book.class, id);
    }

    @Transactional
    public void delete(Integer id) {
        if (id == null) {
            return;
        }
        Session session = sessionFactory.getCurrentSession();
        Book personToBeDeleted = session.get(Book.class, id);
        session.remove(personToBeDeleted);
    }

    @Transactional
    public void createOrUpdate(Book book) {
        Session session = sessionFactory.getCurrentSession();
        book = (Book)session.merge(book);
        session.persist(book);
    }
}
