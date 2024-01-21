package com.mathsena.stockorderapi.service.impl;

import com.mathsena.stockorderapi.dto.ItemDTO;
import com.mathsena.stockorderapi.mappers.ItemMapper;
import com.mathsena.stockorderapi.model.Item;
import com.mathsena.stockorderapi.repository.ItemRepository;
import com.mathsena.stockorderapi.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;
  private final ItemMapper itemMapper;

  public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
  }

  @Override
  public List<ItemDTO> getAllItems() {
    return itemRepository.findAll().stream()
        .map(itemMapper::toDto) // Use a lambda expression here
        .collect(Collectors.toList());
  }

  @Override
  public Item getItemById(Long id) {
    Optional<Item> item = itemRepository.findById(id);
    return item.orElse(null);
  }

  @Override
  public Item createItem(ItemDTO newItemDTO) {
    Item item = itemMapper.toEntity(newItemDTO);
    return itemRepository.save(item);
  }

  @Override
  public Item updateItem(Long id, ItemDTO updatedItemDTO) {
    Optional<Item> itemOptional = itemRepository.findById(id);
    if (itemOptional.isPresent()) {
      Item item = itemOptional.get();
      itemMapper.setItem(updatedItemDTO, item);
      return itemRepository.save(item);
    }
    return null;
  }

  @Override
  public boolean deleteItem(Long id) {
    if (itemRepository.existsById(id)) {
      itemRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
