package com.springtest.crudrest.dao;

import com.springtest.crudrest.models.Person;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PeopleDao {
    private final SessionFactory sessionFactory;
    @Autowired
    public PeopleDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> collectPeople() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Person", Person.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Person loadByPk(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional(readOnly = true)
    public Person loadByPk(Integer id, boolean loadBooks) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        if (loadBooks) {
            Hibernate.initialize(person.getBooks());
        }
        return person;
    }

    @Transactional(readOnly = true)
    public Person loadByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        List<Person> person = session.createQuery("from Person where email = :email", Person.class).setParameter("email", email).getResultList();
        return person.isEmpty() ? null : person.get(0);
    }

    @Transactional
    public void delete(Integer id) {
        if (id == null) {
            return;
        }
        Session session = sessionFactory.getCurrentSession();
        Person personToBeDeleted = session.get(Person.class, id);
        session.remove(personToBeDeleted);
    }

    @Transactional
    public void createOrUpdate(Person person) {
        Session session = sessionFactory.getCurrentSession();
        person = (Person)session.merge(person);
        session.persist(person);
    }
}
