package org.youssefhergal.my_app_ws.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.youssefhergal.my_app_ws.requests.UserRequest;
import org.youssefhergal.my_app_ws.responses.UserResponse;
import org.youssefhergal.my_app_ws.services.UserService;
import org.youssefhergal.my_app_ws.shared.dto.UserDto;

@RestController
@RequestMapping("/users") //localhost:8080/users
public class UserController {

    @Autowired
    UserService userService ;

    @GetMapping
    public String getUser(){
        return "Hello World get user was called ";
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequest , userDto);

        UserDto createdUser = userService.createUser(userDto);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(createdUser , userResponse);

        return userResponse;
    }

    @PutMapping
    public String updateUser(){
        return "Hello World update user was called ";
    }

    @DeleteMapping
    public String deleteUser(){
        return "Hello World delete user was called ";
    }

}
