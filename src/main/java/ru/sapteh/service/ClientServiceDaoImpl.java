package ru.sapteh.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.ClientService;

import java.util.List;


public class ClientServiceDaoImpl implements Dao<ClientService,Integer> {

    private final SessionFactory factory;
    public ClientServiceDaoImpl(SessionFactory factory){
        this.factory=factory;
    }

    @Override
    public void create(ClientService clientService) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.save(clientService);
            session.getTransaction().commit();
        }
    }
    @Override
    public void update(ClientService clientService) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.update(clientService);
            session.getTransaction().commit();
        }
    }
    @Override
    public void delete(ClientService clientService) {
        try(Session session=factory.openSession()) {
            session.beginTransaction();
            session.delete(clientService);
            session.getTransaction().commit();
        }
    }
    @Override
    public List<ClientService> readByAll() {
        try(Session session= factory.openSession()) {
            Query<ClientService> clientServiceQuery=session.createQuery("FROM ClientService ");
            return clientServiceQuery.list();
        }
    }
    @Override
    public ClientService read(Integer id) {
        try(Session session=factory.openSession()) {
            ClientService clientService=session.get(ClientService.class,id);
            return clientService;
        }
    }
}
