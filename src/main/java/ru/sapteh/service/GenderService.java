package ru.sapteh.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.Gender;

import java.util.List;

public class GenderService  implements Dao<Gender,Integer> {


    private final SessionFactory factory;

    public GenderService(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void create(Gender gender) {

    }

    @Override
    public void update(Gender gender) {

    }

    @Override
    public void delete(Gender gender) {

    }

    @Override
    public List<Gender> readByAll() {
       try(Session session= factory.openSession()) {
           Query<Gender> genderQuery=session.createQuery("FROM Gender ");
           return genderQuery.list();
       }
    }

    @Override
    public Gender read(Integer integer) {
        return null;
    }
}
