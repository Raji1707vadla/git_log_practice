package com.example.Phone.Pay.management.controller;

import com.example.Phone.Pay.management.dto.AccountDto;
import com.example.Phone.Pay.management.dto.GenericResponse;
import com.example.Phone.Pay.management.dto.UserDto;
import com.example.Phone.Pay.management.service.ServiceInt;
import com.example.Phone.Pay.management.usage_classes.SelfTransfer;
import com.example.Phone.Pay.management.usage_classes.SignInDetails;
import com.example.Phone.Pay.management.usage_classes.ToMobileNumber;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phone-pay")
public class Controller {
    @Autowired
    ServiceInt serviceInt;

    @PostMapping("/sign-up")
    GenericResponse signUp(@RequestBody UserDto request) {
        return serviceInt.signUp(request);
    }

    @PostMapping("/get-otp/{mobile}")
    GenericResponse getOtp(@PathVariable String mobile) {
        return serviceInt.getOtp(mobile);
    }

    @PostMapping("/sign-in")
    GenericResponse signIn(@RequestBody SignInDetails signInDetails) throws JOSEException {
        return serviceInt.signIn(signInDetails);
    }

    @GetMapping("/getMyAccounts")
    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    GenericResponse getMyAccounts() {
        return serviceInt.getMyAccounts();
    }

    @PostMapping("/addAccountToUser")
    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    GenericResponse addAccountToUser(@RequestBody AccountDto accountDto) {
        return serviceInt.addAccountToUser(accountDto);
    }

    @PutMapping("/withdraw/{accountNumber}/{amount}")
    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    GenericResponse withdraw(@PathVariable String accountNumber, @PathVariable Long amount) {
        return serviceInt.withdraw(accountNumber, amount);
    }

    @PostMapping("/toSelfTransfer")
    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    GenericResponse toSelfTransfer(@RequestBody SelfTransfer selfTransfer) {
        return serviceInt.toSelfTransfer(selfTransfer);
    }

    @PostMapping("/setDefaultAccount/{accountNumber}")
    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    GenericResponse setDefaultAccount(@PathVariable String accountNumber) {
        return serviceInt.setDefaultAccount(accountNumber);
    }

    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    @GetMapping("/get-my-balance/{accountNumber}/{pin}")
    public GenericResponse fetchByBalance(@PathVariable String accountNumber,@PathVariable String pin) {
        return serviceInt.fetchByBalance(accountNumber,pin);
    }
    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    @GetMapping("/set-status-default/{accountNumber}")
    public    GenericResponse setStatusDefault(@PathVariable String accountNumber){
        return serviceInt.setStatusDefault(accountNumber);
    }
   /* @PostMapping("/to-mobile-number")
    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    public GenericResponse toMobileNumber(@RequestBody ToMobileNumber mobileNo) {
        return serviceInt.toMobileNumber(mobileNo);
    }*/// undi
    @PostMapping("/to-mobile-number-transferred")
    @PreAuthorize("hasAnyAuthority('PAID_USER','UNPAID_USER')")
    public GenericResponse toMobileNumberTransferred(@RequestBody ToMobileNumber toMobileNumber){
        return serviceInt.toMobileNumberTransferred(toMobileNumber);
    }


    }
