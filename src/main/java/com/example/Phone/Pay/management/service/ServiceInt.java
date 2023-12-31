package com.example.Phone.Pay.management.service;


import com.example.Phone.Pay.management.dto.*;
import com.example.Phone.Pay.management.usage_classes.SelfTransfer;
import com.example.Phone.Pay.management.usage_classes.SignInDetails;
import com.example.Phone.Pay.management.usage_classes.ToMobileNumber;
import com.nimbusds.jose.JOSEException;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.List;

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

   // List<GitDto> getAll() throws GitAPIException;

  //  List<GitDto> getAll() throws GitAPIException, IOException;

    List<GitDto> getAll(RepoDto dto) throws GitAPIException, IOException;
}
