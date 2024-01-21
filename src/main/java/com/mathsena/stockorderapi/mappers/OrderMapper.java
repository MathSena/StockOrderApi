package com.mathsena.stockorderapi.mappers;

import com.mathsena.stockorderapi.dto.OrderDTO;
import com.mathsena.stockorderapi.model.Order;
import com.mathsena.stockorderapi.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, User.class})
public interface OrderMapper {

  OrderDTO toDto(Order order);

  Order toEntity(OrderDTO orderDto);
}


