package org.youssefhergal.my_app_ws.controllers;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youssefhergal.my_app_ws.entities.UserEntity;
import org.youssefhergal.my_app_ws.exceptions.UserException;
import org.youssefhergal.my_app_ws.requests.UserRequest;
import org.youssefhergal.my_app_ws.responses.ErrorMessages;
import org.youssefhergal.my_app_ws.responses.UserResponse;
import org.youssefhergal.my_app_ws.services.UserService;
import org.youssefhergal.my_app_ws.shared.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {

        UserDto userDto = userService.getUserByUserId(id);

        UserResponse userResponse = new UserResponse();

        BeanUtils.copyProperties(userDto, userResponse);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping(
            value = "/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        if (userRequest.getFirstname().isEmpty())
            throw new UserException(ErrorMessages.MISSING_REQUIRED_FIELDS.getMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequest, userDto);

        UserDto createdUser = userService.createUser(userDto);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(createdUser, userResponse);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }


    @PutMapping(
            path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequest, userDto);

        UserDto updateUser = userService.updateUser(id, userDto);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(updateUser, userResponse);

        return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public List<UserResponse> getAllUsers(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "3") int limit) {
        List<UserResponse> userResponse = new ArrayList<>();

        List<UserDto> users = userService.getAllUsers(page, limit);
        for (UserDto user : users) {
            UserResponse userResponse1 = new UserResponse();
            BeanUtils.copyProperties(user, userResponse1);
            userResponse.add(userResponse1);
        }
        return userResponse;
    }


}