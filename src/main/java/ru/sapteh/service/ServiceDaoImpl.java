package ru.sapteh.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.Service;

import java.util.List;

public class ServiceDaoImpl implements Dao<Service,Integer> {

    private final SessionFactory factory;
    public ServiceDaoImpl(SessionFactory factory){
        this.factory=factory;
    }


    @Override
    public void create(Service service) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.save(service);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Service service) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.update(service);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Service service) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.delete(service);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Service> readByAll() {
       try(Session session=factory.openSession()) {
           Query<Service> serviceQuery=session.createQuery("FROM Service ");
           return serviceQuery.list();
       }
    }

    @Override
    public Service read(Integer id) {
        try(Session session=factory.openSession()) {
            Service service=session.get(Service.class,id);
            return service;

        }
    }
}
