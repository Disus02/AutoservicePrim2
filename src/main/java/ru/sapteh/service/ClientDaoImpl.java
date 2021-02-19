package ru.sapteh.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.Client;

import java.util.List;

public class ClientDaoImpl implements Dao<Client,Integer> {

    private final SessionFactory factory;
    public ClientDaoImpl(SessionFactory factory){
        this.factory=factory;
    }

    public void create(Client client) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();
        }
    }
    public void update(Client client) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
        }
    }
    public void delete(Client client) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.delete(client);
            session.getTransaction().commit();
        }
    }
    public List<Client> readByAll() {
       try(Session session= factory.openSession()) {
           Query<Client> clientQuery=session.createQuery("FROM Client ");
           return clientQuery.list();
       }
    }

    public Client read(Integer id) {
        try(Session session=factory.openSession()) {
            Client client=session.get(Client.class,id);
            return client;

        }
    }
}
