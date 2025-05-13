package org.youssefhergal.my_app_ws.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.youssefhergal.my_app_ws.entities.UserEntity;
import org.youssefhergal.my_app_ws.repositories.UserRepository;
import org.youssefhergal.my_app_ws.shared.dto.UserDto;
import org.youssefhergal.my_app_ws.shared.dto.Utils;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        UserEntity checkuser = userRepository.findByEmail(user.getEmail());
        if (checkuser != null) throw new RuntimeException("User already exists");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(Utils.generateUID(20, true, true));

        UserEntity newUser = userRepository.save(userEntity);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(newUser, userDto);

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), 
                       userEntity.getEncryptedPassword(), 
                       new ArrayList<>());
    }
}