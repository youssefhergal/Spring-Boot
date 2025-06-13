package org.youssefhergal.my_app_ws.controllers;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youssefhergal.my_app_ws.requests.AddressRequest;
import org.youssefhergal.my_app_ws.responses.AddressResponse;
import org.youssefhergal.my_app_ws.services.AddressService;
import org.youssefhergal.my_app_ws.shared.dto.AddressDto;
import jakarta.validation.Valid;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses(Principal principal) {
        List<AddressDto> addresses = addressService.getAllAddresses(principal.getName());

        Type listType = new TypeToken<List<AddressResponse>>() {
        }.getType();

        List<AddressResponse> addressResponses = new ModelMapper().map(addresses, listType);
        return new ResponseEntity<>(addressResponses, HttpStatus.OK);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<AddressResponse> storeAddress(@RequestBody @Valid AddressRequest addressRequest, Principal principal) {
        ModelMapper modelMapper = new ModelMapper();
        AddressDto addressDto = modelMapper.map(addressRequest, AddressDto.class);

        AddressDto storedAddress = addressService.createAddress(addressDto, principal.getName());

        AddressResponse addressResponse = modelMapper.map(storedAddress, AddressResponse.class);
        return new ResponseEntity<>(addressResponse, HttpStatus.CREATED);
    }

    @GetMapping(
            path = "/{addressId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<AddressResponse> getOneAddress(@PathVariable String addressId, Principal principal) {
        AddressDto addressDto = addressService.getAddress(addressId, principal.getName());

        ModelMapper modelMapper = new ModelMapper();
        AddressResponse addressResponse = modelMapper.map(addressDto, AddressResponse.class);

        return new ResponseEntity<>(addressResponse, HttpStatus.OK);
    }


    @DeleteMapping(
            path = "/{addressId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> deleteAddress(@PathVariable String addressId, Principal principal) {
        addressService.deleteAddress(addressId, principal.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}