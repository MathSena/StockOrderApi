package com.mathsena.stockorderapi.service.impl;

import com.mathsena.stockorderapi.dto.ItemDTO;
import com.mathsena.stockorderapi.mappers.ItemMapper;
import com.mathsena.stockorderapi.model.Item;
import com.mathsena.stockorderapi.repository.ItemRepository;
import com.mathsena.stockorderapi.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            .map(itemMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public Optional<ItemDTO> getItemById(Long id) {
    return itemRepository.findById(id)
            .map(itemMapper::toDto);
  }

  @Override
  public ItemDTO createItem(ItemDTO newItemDTO) {
    Item item = itemMapper.toEntity(newItemDTO);
    return itemMapper.toDto(itemRepository.save(item));
  }

  @Transactional
  @Override
  public Optional<ItemDTO> updateItem(Long id, ItemDTO updatedItemDTO) {
    return itemRepository.findById(id).map(item -> {
      itemMapper.setItem(updatedItemDTO, item);
      return itemMapper.toDto(itemRepository.save(item));
    });
  }

  @Transactional
  @Override
  public boolean deleteItem(Long id) {
    if (itemRepository.existsById(id)) {
      itemRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
