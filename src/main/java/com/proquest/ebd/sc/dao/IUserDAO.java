package com.proquest.ebd.sc.dao;

import com.proquest.ebd.sc.model.User;

public interface IUserDAO {
	User findByUserName(String userName);
}
