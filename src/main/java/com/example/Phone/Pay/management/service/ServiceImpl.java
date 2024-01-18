package com.example.Phone.Pay.management.service;
import com.example.Phone.Pay.management.config.JwtTokenUtils;
import com.example.Phone.Pay.management.dto.*;
import com.example.Phone.Pay.management.entity.Account;
import com.example.Phone.Pay.management.entity.User;
import com.example.Phone.Pay.management.repo.AccountRepo;
import com.example.Phone.Pay.management.repo.UserRepo;
import com.example.Phone.Pay.management.usage_classes.OtpStore;
import com.example.Phone.Pay.management.usage_classes.SelfTransfer;
import com.example.Phone.Pay.management.usage_classes.SignInDetails;
import com.example.Phone.Pay.management.usage_classes.ToMobileNumber;
import com.nimbusds.jose.JOSEException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ServiceImpl implements ServiceInt {
    @Autowired
    UserRepo userRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    Account dtoToaccount(AccountDto accountDto) {
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setBalance(accountDto.getBalance());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setAccountType(accountDto.getAccountType());
        account.setStatus(accountDto.getStatus());
        account.setPin(Base64.getEncoder().encodeToString(accountDto.getPin().getBytes()));
        if (accountDto.getUser() != null) account.setUser(dtoToUserC(accountDto.getUser()));
        return account;
    }


    AccountDto accountToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setBalance(account.getBalance());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setStatus(account.getStatus());
        if (accountDto.getUser() != null) accountDto.setUser(userCToDto(account.getUser()));
        return accountDto;
    }


    public User dtoToUserC(UserDto request) {
        User user = new User();
        user.setId(request.getId());
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setMobile(request.getMobile());
        user.setRoleType(request.getRoleType());
        user.setAccountList(request.getAccountDtoList().stream().map(this::dtoToaccount).toList());
        return user;
    }


    public UserDto userCToDto(User entity) {
        UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setName(entity.getName());
        userDto.setAge(entity.getAge());
        userDto.setMobile(entity.getMobile());
        userDto.setRoleType(entity.getRoleType());
        userDto.setAccountDtoList(entity.getAccountList().stream().map(this::accountToDto).toList());
        return userDto;
    }

    @Override
    public GenericResponse signIn(SignInDetails signInDetails) throws JOSEException {
        User user = userRepo.findByMobile(signInDetails.getMobile());
        if (user != null) {
            if (OtpStore.otp.equals(signInDetails.getOtp())) {
                SignInResponseDto response = new SignInResponseDto(user);
                response.setToken(jwtTokenUtils.getToken(user));
                return new GenericResponse(HttpStatus.OK.value(), response);
            } else return new GenericResponse(HttpStatus.UNAUTHORIZED.value(), "wrong otp", "FAILED");
        }
        return new GenericResponse(HttpStatus.UNAUTHORIZED.value(), "no user found with given mobile", "FAILED");
    }

    @Override
    public GenericResponse getOtp(String mobile) {
        User user = userRepo.findByMobile(mobile);
        if (user != null) {
            int min = 1000; // Minimum value of range
            int max = 9999; // Maximum value of range
            int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
            OtpStore.otp = random_int;
            OtpStore.mobile = mobile;
            return new GenericResponse(HttpStatus.OK.value(), "your otp is " + random_int);
        }
        return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "no user found with given mobile");
    }

    @Override
    public GenericResponse signUp(UserDto request) {
        User user = new User();
        if (userRepo.findByMobile(request.getMobile()) == null) {
            user.setName(request.getName());
            user.setAge(request.getAge());
            user.setMobile(request.getMobile());
            user.setRoleType(request.getRoleType());
            userRepo.save(user);
            return new GenericResponse(HttpStatus.OK.value(), "Registration is done", "SUCCESS");
        }
        return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "mobile already exists");
    }


    @Override
    public GenericResponse addAccountToUser(AccountDto accountDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAccountList().stream().noneMatch(e -> e.getAccountNumber().equals(accountDto.getAccountNumber())) && accountRepo.findAll().stream().noneMatch(e -> e.getAccountNumber().equals(accountDto.getAccountNumber()))) {
            user.getAccountList().add(dtoToaccount(accountDto));
            user.setAccountList(user.getAccountList().stream().peek(e -> e.setUser(user)).toList());
            userRepo.save(user);
            return new GenericResponse(HttpStatus.OK.value(), "success");
        }
        return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "failed");
    }


    @Override
    public GenericResponse getMyAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getAccountList().size() != 0) {
            return new GenericResponse(HttpStatus.OK.value(), user.getAccountList().stream().map(this::accountToDto).toList());
        } else return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "no accounts found");
    }

    @Override
    public GenericResponse withdraw(String accountNumber, Long amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getAccountList().stream().anyMatch(e -> e.getAccountNumber().equals(accountNumber) && e.getBalance() > amount)) {
            user.setAccountList(user.getAccountList().stream().peek(e -> {
                if (e.getAccountNumber().equals(accountNumber)) {
                    e.setBalance(e.getBalance() - amount);
                }
            }).toList());
            userRepo.save(user);
            return new GenericResponse(HttpStatus.OK.value(), "success");
        } else
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), user.getAccountList().stream().anyMatch(e -> e.getAccountNumber().equals(accountNumber)) ? "insufficient balance" : "tou don't have account with this number");
    }


    @Override
    public GenericResponse toSelfTransfer(SelfTransfer selfTransfer) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAccountList().stream().noneMatch(e -> e.getAccountNumber().equals(selfTransfer.getAccountFrom()) || e.getAccountNumber().equals(selfTransfer.getAccountTo()))) {
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "no account found");
        } else if (user.getAccountList().stream().filter(e -> e.getAccountNumber().equals(selfTransfer.getAccountFrom())).findFirst().get().getBalance() < selfTransfer.getAmount()) {
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "insufficient funds");
        } else if (user.getAccountList().stream().filter(e -> e.getAccountNumber().equals(selfTransfer.getAccountFrom())).findFirst().get().getPin().equals(selfTransfer.getPin())) {
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "wrong pin");
        }
        user.setAccountList(user.getAccountList().stream().peek(e -> {
            if (e.getAccountNumber().equals(selfTransfer.getAccountFrom()))
                e.setBalance(e.getBalance() - selfTransfer.getAmount());
            else if (e.getAccountNumber().equals(selfTransfer.getAccountTo())) {
                e.setBalance(e.getBalance() + selfTransfer.getAmount());
            }
        }).toList());
        return new GenericResponse(HttpStatus.OK.value(), "success");

       /* Account fromAccount = accountRepo.findByUserAndAccountNumber(user, selfTransfer.getAccountFrom());
        Account toAccount = accountRepo.findByUserAndAccountNumber(user, selfTransfer.getAccountTo());
        if (fromAccount != null) {
            if (!selfTransfer.getPin().equals(new String(Base64.getDecoder().decode(fromAccount.getPin()))))
                return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "pin not matched");
            if (toAccount != null) {
                if (fromAccount.getBalance() > selfTransfer.getAmount()) {
                    fromAccount.setBalance(fromAccount.getBalance() - selfTransfer.getAmount());
                    toAccount.setBalance(toAccount.getBalance() + selfTransfer.getAmount());
                    accountRepo.saveAll(List.of(fromAccount, toAccount));
                    return new GenericResponse(HttpStatus.OK.value(), "successfully transferred");
                } else return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "insufficient balance");
            } else return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "destination account not found");
        } else return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "source account not found");*/
    }

    @Override
    public GenericResponse setDefaultAccount(String accountNumber) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAccountList().stream().noneMatch(e -> e.getAccountNumber().equals(accountNumber))) {
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "no account found");
        }
        user.setAccountList(user.getAccountList().stream().peek(e -> {
            if (e.getAccountNumber().equals(accountNumber)) e.setStatus("default");
            else e.setStatus("not-default");
        }).toList());
        return new GenericResponse(HttpStatus.OK.value(), "set default");
    }

    @Override
    public GenericResponse fetchByBalance(String accountNumber, String pin) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepo.findByUserAndAccountNumber(user, accountNumber);
        if (bCryptPasswordEncoder.matches(pin, account.getPin())) {
            return new GenericResponse(HttpStatus.OK.value(), account.getBalance());
        } else {
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "pin is not correct");
        }
    }

    @Override
    public GenericResponse setStatusDefault(String accountNumber) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Account> account = Optional.ofNullable(accountRepo.findByUserAndAccountNumber(user, accountNumber));
        if (account.isPresent()) {
            Account account1 = account.get();
            account1.setStatus("Default");
            accountRepo.save(account1);
            return new GenericResponse(HttpStatus.OK.value(), "updated successfully...");
        } else {
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "not found..");
        }
    }


    @Override
    public GenericResponse toMobileNumberTransferred(ToMobileNumber toMobileNumber) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userRepo.findByMobile(toMobileNumber.getMobileNo()) == null)
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "no user found with mobile");
        if (user.getAccountList().isEmpty())
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "u didn't have account");
        if (userRepo.findByMobile(toMobileNumber.getMobileNo()).getAccountList().isEmpty())
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "receiver didn't have account");
        Account fromAccount = accountRepo.findByUserAndAccountNumber(user, toMobileNumber.getAccountNumber());
        if (!fromAccount.getPin().equals(Base64.getEncoder().encodeToString(toMobileNumber.getPin().getBytes())))
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "wrong pin");
        Account toAccount = userRepo.findByMobile(toMobileNumber.getMobileNo()).getAccountList().stream().filter(e -> e.getStatus().equalsIgnoreCase("default")).findFirst().get();
        if (fromAccount.getBalance() < toMobileNumber.getBalance())
            return new GenericResponse(HttpStatus.BAD_REQUEST.value(), "insufficient funds");
        fromAccount.setBalance(fromAccount.getBalance() - toMobileNumber.getBalance());
        toAccount.setBalance(toAccount.getBalance() + toMobileNumber.getBalance());
        accountRepo.saveAll(List.of(fromAccount, toAccount));
        return new GenericResponse(HttpStatus.OK.value(), "sent successfully");
    }

    @Override
    public List<GitDto> getAll(RepoDto repoDto) throws GitAPIException, IOException {
        try (Git git = cloneRepository(repoDto.getRepo())) {
      //      Iterable<RevCommit> commits = getCommits(git);
            List<GitDto> gitDtoList = new ArrayList<>();
         /*   for (RevCommit commit : commits) {
                GitDto dto = new GitDto();
                String commitMessage = commit.getFullMessage();
                String authorName = commit.getAuthorIdent().getName();
                Date commitTime = new Date(commit.getCommitTime() * 1000L);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedTime = dateFormat.format(commitTime);

                dto.setUserName(authorName);
                dto.setCommitMessage(commitMessage);
                dto.setCommitTime(formattedTime);
                gitDtoList.add(dto);
            }*/
            return gitDtoList;
        }
    }

    private Git cloneRepository(String repoUrl) throws GitAPIException, IOException {
        File localRepo = new File("git_log_practice");

        // Delete the existing directory if it exists
        if (localRepo.exists()) {
            FileUtils.deleteDirectory(localRepo);
        }

        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(repoUrl);

        return cloneCommand.call();
    }

/*
    private Iterable<RevCommit> getCommits(Git git) throws GitAPIException {
        return git.log().call();
    }
*/

}
