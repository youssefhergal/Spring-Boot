package org.youssefhergal.my_app_ws.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.youssefhergal.my_app_ws.entities.AddressEntity;
import org.youssefhergal.my_app_ws.entities.UserEntity;
import org.youssefhergal.my_app_ws.repositories.AddressRepository;
import org.youssefhergal.my_app_ws.repositories.UserRepository;
import org.youssefhergal.my_app_ws.shared.dto.AddressDto;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public List<AddressDto> getAllAddresses(String email) {

        UserEntity currentUser = userRepository.findByEmail(email);

        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        List<AddressEntity> addresses = currentUser.getAdmin() ?
                (List<AddressEntity>) addressRepository.findAll() :
                addressRepository.findByUser(currentUser);

        Type listType = new TypeToken<List<AddressDto>>() {
        }.getType();

        return new ModelMapper().map(addresses, listType);
    }

    @Override
    public AddressDto createAddress(AddressDto addressDto, String name) {
        UserEntity currentUser = userRepository.findByEmail(name);

        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + name);
        }

        ModelMapper modelMapper = new ModelMapper();
        AddressEntity addressEntity = modelMapper.map(addressDto, AddressEntity.class);
        addressEntity.setUser(currentUser);
        addressEntity.setAddressId(UUID.randomUUID().toString());

        AddressEntity storedAddress = addressRepository.save(addressEntity);

        return modelMapper.map(storedAddress, AddressDto.class);
    }

    @Override
    public AddressDto getAddress(String addressId, String name) {
        UserEntity currentUser = userRepository.findByEmail(name);

        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + name);
        }

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if (addressEntity == null) {
            throw new RuntimeException("Address not found with ID: " + addressId);
        }

        if (!currentUser.getAdmin() && !addressEntity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("User not authorized to access this address");
        }

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressEntity, AddressDto.class);


    }

    @Override
    public void deleteAddress(String addressId, String name) {
        UserEntity currentUser = userRepository.findByEmail(name);

        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + name);
        }

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if (addressEntity == null) {
            throw new RuntimeException("Address not found with ID: " + addressId);
        }

        if (!currentUser.getAdmin() && !addressEntity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("User not authorized to delete this address");
        }

        addressRepository.delete(addressEntity);
    }
}
