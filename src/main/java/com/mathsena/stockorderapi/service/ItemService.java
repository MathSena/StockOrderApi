package com.mathsena.stockorderapi.service;

import com.mathsena.stockorderapi.dto.ItemDTO;
import com.mathsena.stockorderapi.model.Item;

import java.util.List;

public interface ItemService {
  List<ItemDTO> getAllItems();

  Item getItemById(Long id);

  Item createItem(ItemDTO newItemDTO);

  Item updateItem(Long id, ItemDTO updatedItemDTO);

  boolean deleteItem(Long id);
}
