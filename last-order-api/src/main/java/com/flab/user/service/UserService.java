package com.flab.user.service;


import com.flab.user.domain.User;
import com.flab.user.dto.LoginRequest;
import com.flab.user.exception.EmailAlreadyExistException;
import com.flab.user.exception.EmailNotExistException;
import com.flab.user.exception.PasswordNotMatchException;
import com.flab.user.repository.JpaAccountRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import static com.flab.common.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JpaAccountRepository userRepository;
    public Boolean signUp(User user) {
        //email이 중복이 되었는가?
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyExistException(EMAIL_ALREADY_EXIST);
        }
        String encrypt = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(encrypt);
        userRepository.save(user);

        return true;
    }
    public void login(LoginRequest login){
        User account;
        if((account = userRepository.findByEmail(login.getEmail())) == null){
            throw new EmailNotExistException(EMAIL_NOT_EXIST);
        }
        if(!BCrypt.checkpw(login.getPassword(),account.getPassword())){
            throw new PasswordNotMatchException(PASSWORD_NOT_MATCH);
        }
    }
}