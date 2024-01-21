package com.mathsena.stockorderapi.mappers;

import com.mathsena.stockorderapi.dto.StockMovementDTO;
import com.mathsena.stockorderapi.model.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
  @Mapping(source = "item.id", target = "itemId")
  StockMovementDTO toDto(StockMovement stockMovement);

  @Mapping(target = "item.id", source = "itemId")
  StockMovement toEntity(StockMovementDTO stockMovementDTO);
}