package org.youssefhergal.my_app_ws.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.youssefhergal.my_app_ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUser(String mail);

    UserDto getUserByUserId(String userId);

    UserDto updateUser(String userId, UserDto userDto);

    void deleteUser(String userId);

}
