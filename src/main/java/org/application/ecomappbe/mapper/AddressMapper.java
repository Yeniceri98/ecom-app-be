package org.application.ecomappbe.mapper;

import org.application.ecomappbe.dto.AddressDto;
import org.application.ecomappbe.model.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto mapToDto(Address address);
    Address mapToEntity(AddressDto addressDto);

    List<AddressDto> mapToDtoList(List<Address> addresses);
    List<Address> mapToEntityList(List<AddressDto> addressDtos);
}
