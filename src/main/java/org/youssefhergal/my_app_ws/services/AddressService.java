package org.youssefhergal.my_app_ws.services;

import org.youssefhergal.my_app_ws.shared.dto.AddressDto;

import java.util.List;


public interface AddressService {

    List<AddressDto> getAllAddresses(String email);

    AddressDto createAddress(AddressDto addressDto, String name);

    AddressDto getAddress(String addressId, String name);

    void deleteAddress(String addressId, String name);
}
