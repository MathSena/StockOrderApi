package com.mathsena.stockorderapi.service;

import com.mathsena.stockorderapi.dto.ItemDTO;

import java.util.List;
import java.util.Optional;

public interface ItemService {
  List<ItemDTO> getAllItems();

  Optional<ItemDTO> getItemById(Long id);

  ItemDTO createItem(ItemDTO newItemDTO);

  Optional<ItemDTO> updateItem(Long id, ItemDTO updatedItemDTO);

  boolean deleteItem(Long id);
}
