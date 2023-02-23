package com.example.community.service;

import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.user.UserDeleteDTO;
import com.example.community.model.DTO.user.UserLoginDTO;
import com.example.community.model.DTO.user.UserSignupDTO;
import com.example.community.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User login(UserLoginDTO userLoginDTO) throws IllegalArgumentException{
        String loginUserId = userLoginDTO.getUserId();
        String loginPassword = userLoginDTO.getPassword();
        String confirmPassword = userLoginDTO.getConfirmPassword();

        Optional<User> optionalUser = userRepository.findByUserId(loginUserId);

        if(optionalUser.isPresent()){
            User findUser = optionalUser.get();

            if(bCryptPasswordEncoder.matches(loginPassword, findUser.getPassword())){
                if(loginPassword.equals(confirmPassword)) {
                    return findUser;
                }else{
                    throw new IllegalArgumentException("password not match for confirm");
                }
            }else{
                return null;
            }
        }
        return null;
    }

    public User signup(UserSignupDTO userSignupDTO) throws IllegalArgumentException, DataIntegrityViolationException {

        if(!(userSignupDTO.isTermsOfInfo() && userSignupDTO.isTermsOfExam())){
            return null;
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userSignupDTO.getPassword());

        User user = new User(
                userSignupDTO.getUserId(),
                encodedPassword,
                userSignupDTO.getName(),
                userSignupDTO.getEmail()
        );

        return userRepository.save(user);
    }

    public boolean delete(UserDeleteDTO userDeleteDTO) throws IllegalArgumentException{
        long id = userDeleteDTO.getId();
        String password = userDeleteDTO.getPassword();
        String confirmPassword = userDeleteDTO.getConfirmPassword();

        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
//            password를 정상적으로 입력했을 때
            if(bCryptPasswordEncoder.matches(password, user.getPassword())){
//                pasword와 confirmPassword가 동일한지 확인함
                if(password.equals(confirmPassword)){
//                    동일하다면 삭제함
                    userRepository.delete(user);

                    Optional<User> deleteOptionalUser = userRepository.findById(user.getId());

                    return deleteOptionalUser.isEmpty();
                }else{
//                    동일하지 않다면 예외를 발생시켜서 사용자에게 입력이 이상하다는 것을 알려줌
                    throw new IllegalArgumentException("password not match for confirm");
                }
            }else{
                return false;
            }
        }
        return false;
    }

    public void change(){

    }

    public void find(){

    }
}
