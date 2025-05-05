package org.application.ecomappbe.mapper;

import org.application.ecomappbe.dto.PaymentDto;
import org.application.ecomappbe.model.Payment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDto mapToDto(Payment payment);
    Payment mapToEntity(PaymentDto paymentDto);

    List<PaymentDto> mapToDtoList(List<Payment> payments);
    List<Payment> mapToEntityList(List<PaymentDto> paymentDtos);
}
