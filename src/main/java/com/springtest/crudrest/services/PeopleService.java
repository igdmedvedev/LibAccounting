package com.springtest.crudrest.services;

import com.springtest.crudrest.models.Person;
import com.springtest.crudrest.repositories.PeopleRepository;
import com.springtest.crudrest.utils.NotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> collectPeople(Integer page) {
        return peopleRepository.findAll(PageRequest.of(page, 6, Sort.by("lastName"))).getContent();
    }
    public List<Person> collectPeople() {
        return peopleRepository.findAll();
    }

    public Person loadByPk(Integer id, boolean loadBooks) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isEmpty()) {
            throw new NotFoundException(String.format("Reader with id = %d wasn't found.", id));
        }
        if (loadBooks) {
            Hibernate.initialize(person.get().getBooks());
        }
        return person.get();
    }

    public Person loadByPk(Integer id) {
        return loadByPk(id, false);
    }

    public Person loadByEmail(String email) {
        List<Person> person = peopleRepository.findByEmail(email);
        if (person == null || person.isEmpty()) {
            return null;
        }
        return person.get(0); // email - UK, so there can't more than one person.
    }
    @Transactional
    public void saveOrUpdate(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(Integer id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void delete(Person person) {
        peopleRepository.delete(person);
    }
}
