package com.example.community.service;

import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.user.*;
import com.example.community.model.VO.UserLoginResultVO;
import com.example.community.model.VO.UserSignupResultVO;
import com.example.community.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public UserLoginResultVO login(UserLoginDTO userLoginDTO)
            throws IllegalArgumentException, NoSuchElementException{
        String loginUserId = userLoginDTO.getUserId();
        String loginPassword = userLoginDTO.getPassword();
        String confirmPassword = userLoginDTO.getConfirmPassword();

        Optional<User> optionalUser = userRepository.findByUserId(loginUserId);

        if(optionalUser.isPresent()){
            User findUser = optionalUser.get();

            if(bCryptPasswordEncoder.matches(loginPassword, findUser.getPassword())){
                if(loginPassword.equals(confirmPassword)) {

                    return UserLoginResultVO.builder()
                            .userId(findUser.getUserId())
                            .name(findUser.getName())
                            .build();

                }else{
                    throw new IllegalArgumentException("password not match for confirm");
                }
            }else{
                throw new IllegalArgumentException("password not match for user");
            }
        }
        throw new NoSuchElementException();
    }

    @Transactional
    public UserSignupResultVO signup(UserSignupDTO userSignupDTO) throws IllegalArgumentException, DataIntegrityViolationException {

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

        User savedUser = userRepository.save(user);

        return UserSignupResultVO.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .build();
    }

    @Transactional
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

    @Transactional
    public User change(long id, UserInfoChangeDTO userInfoChangeDTO) throws IllegalArgumentException{
        blankConvert(userInfoChangeDTO);
//        모든 인수가 blank이면 예외를 발생
        if(allArgumentBlank(userInfoChangeDTO)){
            throw new IllegalArgumentException("all argument must not be blank");
        }else {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                User editUser = editUserInfo(user, userInfoChangeDTO);

                return userRepository.save(editUser);
            }
            return null;
        }
    }

    @Transactional(readOnly = true)
    public String findUserId(FindUserIdDTO findUserIdDTO) throws IllegalArgumentException{
        String email = findUserIdDTO.getEmail();
        String name = findUserIdDTO.getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {

            User findUser = optionalUser.get();

            String findName = findUser.getName();

            if (findName.equals(name)) {
                return findUser.getUserId();
            } else {
                throw new IllegalArgumentException("This name not match for name to user");
            }

        }else{
            return null;
        }
    }

    @Transactional
    public String findPassword(FindPasswordDTO findPasswordDTO) throws IllegalArgumentException, NoSuchElementException{
        String email = findPasswordDTO.getEmail();
        String userId = findPasswordDTO.getUserId();

        User findUser = userRepository.findByUserId(userId)
                .orElseThrow(NoSuchElementException::new);

        String findEmail = findUser.getEmail();

        if(findEmail.equals(email)){
            String changePassword = UUID.randomUUID().toString().substring(0, 6);

            findUser.changePassword(changePassword);
            userRepository.save(findUser);

            return changePassword;
        }else{
            throw new IllegalArgumentException("This email not match for email to user");
        }
    }

    /**
     * 모든 인수가 blank인지 확인
     */
    private boolean allArgumentBlank(UserInfoChangeDTO userInfoChangeDTO){
        return userInfoChangeDTO.getPassword().isBlank() &&
                userInfoChangeDTO.getName().isBlank() &&
                userInfoChangeDTO.getEmail().isBlank();
    }

    private UserInfoChangeDTO blankConvert(UserInfoChangeDTO beforeDTO){
        if(beforeDTO.getPassword() == null){
            beforeDTO.setPassword("");
        }
        if(beforeDTO.getConfirmPassword() == null){
            beforeDTO.setConfirmPassword("");
        }
        if(beforeDTO.getName() == null){
            beforeDTO.setName("");
        }
        if(beforeDTO.getEmail() == null){
            beforeDTO.setEmail("");
        }
        return beforeDTO;

    }

    private User editUserInfo(User user, UserInfoChangeDTO userInfoChangeDTO){
        String changePassword = userInfoChangeDTO.getPassword();
        String changeConfirmPassword = userInfoChangeDTO.getConfirmPassword();
        String changeName = userInfoChangeDTO.getName();
        String changeEmail = userInfoChangeDTO.getEmail();

        if(isNotBlank(changePassword)){
            if(changePassword.equals(changeConfirmPassword)){
                String encodedPassword = bCryptPasswordEncoder.encode(changePassword);

                user.changePassword(encodedPassword);
            }else{
                throw new IllegalArgumentException("password not match for confirm");
            }
        }
        if(isNotBlank(changeName)) user.changeName(changeName);

        if(isNotBlank(changeEmail)) user.changeEmail(changeEmail);

        return user;
    }

    private boolean isNotBlank(String content){
        return !content.isBlank();
    }
}
