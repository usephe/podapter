package com.audiophileproject.usermanagement.services;

import com.audiophileproject.usermanagement.models.User;
import com.audiophileproject.usermanagement.repos.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private  final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    /**
     * if user exist, update it. else, create it.
     * @param user
     * @return
     */
    @Transactional
    public User updateUser(User user) {
        User currUser = (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            userRepository
                    .findById(currUser.getId()) // returns Optional<User>
                    .ifPresent(oldUser -> {
                        oldUser.setFirstname(user.getFirstname());
                        oldUser.setLastname(user.getLastname());
                        oldUser.setUsername(user.getUsername());
                        oldUser.setEmail(user.getEmail());
                        userRepository.save(oldUser);
                    });

        return  userRepository.findById(currUser.getId()).orElseThrow(()-> new RuntimeException());
    }

    public void deleteUser(String username){
        userRepository.deleteByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found !"));
    }

    public User loadUserByUserId(String userId) {
        return userRepository.findByUsername(userId).orElseThrow();
    }
}
