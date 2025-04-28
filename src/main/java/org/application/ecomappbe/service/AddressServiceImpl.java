package org.application.ecomappbe.service;

import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.dto.AddressDto;
import org.application.ecomappbe.exception.ResourceAccessDeniedException;
import org.application.ecomappbe.exception.ResourceAlreadyExistsException;
import org.application.ecomappbe.exception.ResourceNotFoundException;
import org.application.ecomappbe.mapper.AddressMapper;
import org.application.ecomappbe.model.Address;
import org.application.ecomappbe.model.User;
import org.application.ecomappbe.repository.AddressRepository;
import org.application.ecomappbe.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final AuthUtil authUtil;

    @Override
    public AddressDto createAddress(AddressDto addressDto) {
        if (addressRepository.existsByStreetNameAndBuildingName(addressDto.getStreetName(), addressDto.getBuildingName())) {
            throw new ResourceAlreadyExistsException("Address already exists!");
        }

        Address address = addressMapper.mapToEntity(addressDto);

        // Get the currently logged-in user and set it to the address
        User currentUser = authUtil.loggedInUser();
        address.setUser(currentUser);

        Address savedAddress = addressRepository.save(address);
        return addressMapper.mapToDto(savedAddress);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addressMapper.mapToDtoList(addresses);
    }

    @Override
    public List<AddressDto> getCurrentUserAddresses() {
        User currentUser = authUtil.loggedInUser();
        List<Address> addresses = addressRepository.findByUser(currentUser);
        return addressMapper.mapToDtoList(addresses);
    }

    @Override
    public AddressDto getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with id: " + addressId)
        );
        return addressMapper.mapToDto(address);
    }

    @Override
    public AddressDto updateAddress(Long addressId, AddressDto addressDto) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with id: " + addressId)
        );

        User currentUser = authUtil.loggedInUser();
        if (!address.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ResourceAccessDeniedException("You can only update your own addresses!");
        }

        address.setStreetName(addressDto.getStreetName());
        address.setBuildingName(addressDto.getBuildingName());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setZipcode(addressDto.getZipcode());

        Address updatedAddress = addressRepository.save(address);
        return addressMapper.mapToDto(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with id: " + addressId)
        );

        User currentUser = authUtil.loggedInUser();
        if (!address.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ResourceAccessDeniedException("You can only delete your own addresses!");
        }

        addressRepository.delete(address);
    }
}
