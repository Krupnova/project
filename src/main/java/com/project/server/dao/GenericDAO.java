package com.project.server.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true, value = "transactionManager")
public abstract class GenericDAO<E, I extends Serializable> {

    protected Class<E> entityClass;

    private SessionFactory sessionFactory;

    public GenericDAO() {
    }

    @SuppressWarnings("unchecked")
    public GenericDAO(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Autowired()
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public void persist(E entity) {
        getCurrentSession().save(entity);
        getCurrentSession().flush();
    }

    @Transactional
    public E findById(I id) {
        return getCurrentSession().get(entityClass, id);
    }

    @Transactional
    public E update(E entity) {
        getCurrentSession().update(entity);
        getCurrentSession().flush();
        return entity;
    }

    @Transactional
    public void delete(E entity) {
        getCurrentSession().delete(entity);
    }

    @Transactional
    public void refresh(E entity) {
        getCurrentSession().refresh(entity);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<E> getAll() {
        Query q = getCurrentSession().createQuery("from " + entityClass.getSimpleName());
        List<E> list = (List<E>) q.getResultList();
        return list == null ? new ArrayList<>() : list;
    }
}
