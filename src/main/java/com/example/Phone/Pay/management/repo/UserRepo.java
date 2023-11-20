package com.example.Phone.Pay.management.repo;

import com.example.Phone.Pay.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByMobile(String mobile);


    User findByAccountListAccountNumberContains(String accountNumber);
}
