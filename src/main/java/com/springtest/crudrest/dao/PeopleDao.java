package com.springtest.crudrest.dao;

import com.springtest.crudrest.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PeopleDao {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PeopleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> collectPeople() {
        return jdbcTemplate.query("SELECT * FROM People", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person loadByPk(Integer id) {
        List<Person> person = jdbcTemplate.query("SELECT * FROM People WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class));
        return person.isEmpty() ? null : person.get(0);
    }

    public Person loadByEmail(String email) {
        return jdbcTemplate.query(
                "SELECT * FROM People WHERE email = ?", new Object[]{email},
                new BeanPropertyRowMapper<>(Person.class)
        ).stream().findAny().orElse(null);
    }

    public void delete(Integer id) {
        if (id == null) {
            return;
        }
        jdbcTemplate.update("delete from People where id = ?", id);
    }

    public void create(Person person) {
        jdbcTemplate.update("insert into People (firstname, lastname, middlename, birthday, email, gender) values (?, ?, ?, ?, ?, ?)",
                person.getFirstName(), person.getLastName(), person.getMiddleName(), person.getBirthday(), person.getEmail(), person.getGender());
    }

    public void update(Person person) {
        jdbcTemplate.update("update People set firstname = ?, lastname = ?, middlename = ?, birthday = ?, email = ?, gender = ? where id = ?",
                person.getFirstName(), person.getLastName(), person.getMiddleName(), person.getBirthday(), person.getEmail(), person.getGender(), person.getId());
    }
}
