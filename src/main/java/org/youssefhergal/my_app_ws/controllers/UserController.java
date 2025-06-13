package org.youssefhergal.my_app_ws.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youssefhergal.my_app_ws.exceptions.UserException;
import org.youssefhergal.my_app_ws.requests.AddressRequest;
import org.youssefhergal.my_app_ws.requests.UserRequest;
import org.youssefhergal.my_app_ws.responses.ErrorMessages;
import org.youssefhergal.my_app_ws.responses.UserResponse;
import org.youssefhergal.my_app_ws.services.UserService;
import org.youssefhergal.my_app_ws.shared.dto.AddressDto;
import org.youssefhergal.my_app_ws.shared.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    private ModelMapper getConfiguredModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configure mapping from AddressRequest.street to AddressDto.streetName
        modelMapper.createTypeMap(AddressRequest.class, AddressDto.class)
                .addMappings(mapper -> mapper.map(AddressRequest::getStreet, AddressDto::setStreet));

        return modelMapper;
    }

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

        // Use ModelMapper for proper address mapping
        ModelMapper modelMapper = getConfiguredModelMapper();
        UserDto userDto = modelMapper.map(userRequest, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        UserResponse userResponse = modelMapper.map(createdUser, UserResponse.class);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }


    @PutMapping(
            path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
//      UserDto userDto = new UserDto();
//      BeanUtils.copyProperties(userRequest, userDto);

        ModelMapper modelMapper = getConfiguredModelMapper();
        UserDto userDto = modelMapper.map(userRequest, UserDto.class);

        UserDto updateUser = userService.updateUser(id, userDto);

        UserResponse userResponse = modelMapper.map(updateUser, UserResponse.class);


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
    public List<UserResponse> getAllUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "3") int limit,
            @RequestParam(value = "search", required = false) String search) {

        List<UserResponse> userResponse = new ArrayList<>();

        List<UserDto> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userService.getAllUsers(page, limit, search.trim());
        } else {
            users = userService.getAllUsers(page, limit);
        }

        for (UserDto user : users) {
            UserResponse userResponse1 = new UserResponse();
            BeanUtils.copyProperties(user, userResponse1);
            userResponse.add(userResponse1);
        }
        return userResponse;
    }


}