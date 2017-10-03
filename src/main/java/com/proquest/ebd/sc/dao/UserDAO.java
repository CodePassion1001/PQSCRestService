package com.proquest.ebd.sc.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.proquest.ebd.sc.model.User;
@Repository
@Scope(value="prototype")
public class UserDAO implements IUserDAO {

	@PersistenceContext	
	private EntityManager entityManager;

	@Override
	public User findByUserName(String userName) {
		return entityManager.find(User.class, userName);
	}
}
