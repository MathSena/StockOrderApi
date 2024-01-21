package com.mathsena.stockorderapi.mappers;

import com.mathsena.stockorderapi.dto.ItemDTO;
import com.mathsena.stockorderapi.model.Item;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDTO toDto(Item item);
    Item toEntity(ItemDTO itemDTO);

    @AfterMapping
    default void setItem(ItemDTO itemDTO, @MappingTarget Item item) {
        item.setName(itemDTO.getName());
    }

}
