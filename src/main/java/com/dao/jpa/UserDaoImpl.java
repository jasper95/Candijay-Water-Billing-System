/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao.jpa;

import com.dao.UserDao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.domain.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bert
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    EntityManager em;
    
    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> listUsers(Class<T> clazz) {
        TypedQuery<T> query = em.createQuery("from "+ clazz.getName(), clazz);
        return query.getResultList();
    }

    @Override
    public void removeUser(String username) {
        User user = em.find(User.class, username);
        em.remove(user);
    }

    @Override
    public User getUserByUsername(String username) {
        Session session = em.unwrap(Session.class);
        Criteria criteria= session.createCriteria(User.class);
        criteria.add(Restrictions.eq("username", username));		
        return (User) criteria.uniqueResult();
    }

    @Override
    public User getUserByAttribute(Object attribute, String fieldName) throws Exception {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq(fieldName, attribute));
        return (User)criteria.uniqueResult();
    }    
}
