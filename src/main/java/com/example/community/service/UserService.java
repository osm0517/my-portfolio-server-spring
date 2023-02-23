package com.example.community.service;

import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.UserLoginDTO;
import com.example.community.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User login(UserLoginDTO userLoginDTO){
        String loginUserId = userLoginDTO.getUserId();
        String loginPassword = userLoginDTO.getPassword();

        Optional<User> optionalUser = userRepository.findByUserId(loginUserId);

        if(optionalUser.isPresent()){
            User findUser = optionalUser.get();

            if(bCryptPasswordEncoder.matches(loginPassword, findUser.getPassword())){
                return findUser;
            }else{
                return null;
            }
        }
        return null;
    }

    public void signup(){

    }

    public void delete(){

    }

    public void change(){

    }

    public void find(){

    }
}
