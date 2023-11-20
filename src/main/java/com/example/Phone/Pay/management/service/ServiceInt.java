package com.example.Phone.Pay.management.service;


import com.example.Phone.Pay.management.dto.AccountDto;
import com.example.Phone.Pay.management.dto.GenericResponse;
import com.example.Phone.Pay.management.dto.UserDto;
import com.example.Phone.Pay.management.usage_classes.SelfTransfer;
import com.example.Phone.Pay.management.usage_classes.SignInDetails;
import com.example.Phone.Pay.management.usage_classes.ToMobileNumber;
import com.nimbusds.jose.JOSEException;

public interface ServiceInt {
    GenericResponse signIn(SignInDetails signInDetails) throws JOSEException;

    GenericResponse getOtp(String mobile);

    GenericResponse signUp(UserDto request);

    GenericResponse addAccountToUser(AccountDto accountDto);

    GenericResponse getMyAccounts();

    GenericResponse withdraw(String accountNumber, Long amount);

    GenericResponse toSelfTransfer(SelfTransfer selfTransfer);

    GenericResponse setDefaultAccount(String accountNumber);
    GenericResponse fetchByBalance(String accountNumber, String pin);
    GenericResponse setStatusDefault(String accountNumber);
    //GenericResponse toMobileNumber(ToMobileNumber mobileNumber);

    GenericResponse toMobileNumberTransferred(ToMobileNumber toMobileNumber);
}
