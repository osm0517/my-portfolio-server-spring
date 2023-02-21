//package com.example.community.utils.jwt;
//
//import com.example.community.repository.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        //user의 계정이 존재하는지 확인
//        String selectResult = userRepository.selectPassword(userId);
//
//        //userId와 일치하는 계정이 없다면 exception을 발생
//        if(Objects.equals(selectResult, null)) throw new UsernameNotFoundException(userId + "NotFound");
//
//        //존재하다면 user의 권한은 어떤 것인지 확인
//        String userAuth = userRepository.findAuthByUserId(userId);
//
//        if(Objects.equals(userAuth, null)) throw new RuntimeException(userAuth + "NotFound");
//
//        return new User(userId, userId, AuthorityUtils.createAuthorityList(userAuth));
//    }
//}
