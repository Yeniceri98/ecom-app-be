package org.application.ecomappbe.service;

import org.application.ecomappbe.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto createAddress(AddressDto addressDto);
    List<AddressDto> getAllAddresses();
    List<AddressDto> getCurrentUserAddresses();
    AddressDto getAddressById(Long addressId);
    AddressDto updateAddress(Long addressId, AddressDto addressDto);
    void deleteAddress(Long addressId);
}
