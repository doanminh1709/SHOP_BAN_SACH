package com.example.qlybanhang.service;

import com.example.qlybanhang.Entity.User;
import com.example.qlybanhang.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //khi login thì nó sẽ tìm xem có thằng user hay không
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User st = userRepository.findUserByUsername(username);
        if(st == null){
            throw new UsernameNotFoundException("Not found");
        }
        List<SimpleGrantedAuthority>list = new ArrayList<SimpleGrantedAuthority>();
        //convert role của mình sang role của thằng security
        for (String role : st.getRole()){
            list.add(new SimpleGrantedAuthority(role));
        }
        //current User : chính là thằng user đăng nhập , lưu trong session
        // nó chỉ lấy thuộc tính của thằng entiy
        org.springframework.security.core.userdetails.User currentUser =
                new org.springframework.security.core.userdetails.User(username , st.getPassword() , list);
        return currentUser;
    }
}
