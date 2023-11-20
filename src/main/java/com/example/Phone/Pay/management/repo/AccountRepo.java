package com.example.Phone.Pay.management.repo;

import com.example.Phone.Pay.management.entity.Account;
import com.example.Phone.Pay.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {

    Account findByUserAndAccountNumber(User user, String accountNumber);

    List<Account> findByUserMobile(String mobileNo);

    Account findByUserAndAccountNumberAndPin(User user, String accountNumber, String pin);

    Account findByAccountNumber(String accountNumber);
}
