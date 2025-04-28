package org.application.ecomappbe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.dto.AddressDto;
import org.application.ecomappbe.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(addressService.createAddress(addressDto), HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return new ResponseEntity<>(addressService.getAllAddresses(), HttpStatus.OK);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDto>> getCurrentUserAddresses() {
        return new ResponseEntity<>(addressService.getCurrentUserAddresses(), HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long addressId) {
        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long addressId, @Valid @RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(addressService.updateAddress(addressId, addressDto), HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return new ResponseEntity<>("Address deleted successfully", HttpStatus.OK);
    }
}
