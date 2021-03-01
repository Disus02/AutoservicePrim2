package ru.sapteh.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.Service;
import ru.sapteh.model.Users;

import java.util.List;

public class UsersDaoImpl implements Dao<Users,Integer> {

    private SessionFactory factory;
    public UsersDaoImpl(SessionFactory factory){
        this.factory=factory;
    }
    @Override
    public void create(Users users) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.save(users);
            session.getTransaction().commit();
        }
    }
    @Override
    public void update(Users users) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.update(users);
            session.getTransaction().commit();
        }
    }
    @Override
    public void delete(Users users) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.delete(users);
            session.getTransaction().commit();
        }
    }
    @Override
    public List<Users> readByAll() {
        try(Session session=factory.openSession()) {
            Query<Users> usersQuery=session.createQuery("FROM Users ");
            return usersQuery.list();
        }
    }
    @Override
    public Users read(Integer id) {
        try(Session session=factory.openSession()) {
            Users users=session.get(Users.class,id);
            return users;
        }
    }
}
