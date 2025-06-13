package org.youssefhergal.my_app_ws.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.youssefhergal.my_app_ws.entities.AddressEntity;
import org.youssefhergal.my_app_ws.entities.ContactEntity;
import org.youssefhergal.my_app_ws.entities.UserEntity;
import org.youssefhergal.my_app_ws.repositories.UserRepository;
import org.youssefhergal.my_app_ws.shared.dto.AddressDto;
import org.youssefhergal.my_app_ws.shared.dto.ContactDto;
import org.youssefhergal.my_app_ws.shared.dto.UserDto;
import org.youssefhergal.my_app_ws.shared.dto.Utils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        // Vérifier si l'utilisateur existe déjà
        UserEntity checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser != null) throw new RuntimeException("User Already Exists!");

        ModelMapper modelMapper = new ModelMapper();

        // Configuration du ModelMapper pour ignorer les IDs des entités et mapper les champs
        modelMapper.createTypeMap(AddressDto.class, AddressEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(AddressEntity::setId);
                    mapper.map(AddressDto::getStreet, AddressEntity::setStreet);
                });
        modelMapper.createTypeMap(ContactDto.class, ContactEntity.class)
                .addMappings(mapper -> mapper.skip(ContactEntity::setId));

        // Mapper le DTO vers l'entité
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        // Configurer les informations de base de l'utilisateur
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(Utils.generateUID(30, false, false));

        // Gérer explicitement le contact
        if (userEntity.getContact() != null) {
            ContactEntity contactEntity = userEntity.getContact();
            contactEntity.setContactId(Utils.generateUID(30, true, true));
            contactEntity.setUser(userEntity);
            userEntity.setContact(contactEntity);
        }


        // Configurer les adresses
        if (userEntity.getAddresses() != null) {
            for (AddressEntity address : userEntity.getAddresses()) {
                address.setUser(userEntity);
                address.setAddressId(Utils.generateUID(30, true, true));
            }
        }

        // Sauvegarder l'utilisateur
        UserEntity newUser = userRepository.save(userEntity);

        // Mapper l'entité sauvegardée vers DTO
        return modelMapper.map(newUser, UserDto.class);
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

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) throw new UsernameNotFoundException(userId);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) throw new UsernameNotFoundException(userId);

        userEntity.setFirstname(userDto.getFirstname());
        userEntity.setLastname(userDto.getLastname());

        userRepository.save(userEntity);

        UserDto updatedUser = new UserDto();

        BeanUtils.copyProperties(userEntity, updatedUser);

        return updatedUser;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) throw new UsernameNotFoundException(userId);
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getAllUsers(int page, int limit, String search) {

        if (page > 0) page -= 1;

        List<UserDto> userDtos = new ArrayList<>();
        Pageable pageable = Pageable.ofSize(limit).withPage(page);

        Page<UserEntity> usersPage;
        if (search != null && !search.isEmpty()) {
            usersPage = userRepository.findAll(pageable, search);
        } else {
            usersPage = userRepository.findAll(pageable);
        }

        List<UserEntity> users = usersPage.getContent();

        for (UserEntity user : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<UserDto> getAllUsers(int page, int limit) {

        if (page > 0) page -= 1;

        List<UserDto> userDtos = new ArrayList<>();
        Pageable pageable = Pageable.ofSize(limit).withPage(page);

        Page<UserEntity> usersPage = userRepository.findAll(pageable);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity user : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userDtos.add(userDto);
        }
        return userDtos;
    }
}