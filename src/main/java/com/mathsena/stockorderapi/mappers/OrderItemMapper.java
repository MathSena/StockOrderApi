package com.mathsena.stockorderapi.mappers;

import com.mathsena.stockorderapi.dto.OrderItemDTO;
import com.mathsena.stockorderapi.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
  @Mapping(source = "item.id", target = "itemId")
  OrderItemDTO toDto(OrderItem orderItem);

  @Mapping(source = "itemId", target = "item.id")
  OrderItem toEntity(OrderItemDTO orderItemDto);
}
