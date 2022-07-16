package com.fengliuwan.staybooking.service;

import com.fengliuwan.staybooking.exception.UserAlreadyExistException;
import com.fengliuwan.staybooking.model.Authority;
import com.fengliuwan.staybooking.model.User;
import com.fengliuwan.staybooking.model.UserRole;
import com.fengliuwan.staybooking.repository.AuthorityRepository;
import com.fengliuwan.staybooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {

    private UserRepository userRepository;

    private AuthorityRepository authorityRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE) //保证对两张表的操作all or nothing. ensure atomic，if one fails， roll back change to the other
    public void add(User user, UserRole role) throws UserAlreadyExistException {

        if (userRepository.existsById(user.getUsername())) { // API provided by spring framework
            throw new UserAlreadyExistException("User already exists");
        }   //注册用户时 先check用户是否已经存在

        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode password before saving to db
        user.setEnabled(true);
        userRepository.save(user);  // save user info to user table
        authorityRepository.save(new Authority(user.getUsername(), role.name())); // insert user authority to authority table
        // role.name() 在enum里面自动实现了
    }
}
