package com.mathsena.stockorderapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathsena.stockorderapi.dto.ItemDTO;
import com.mathsena.stockorderapi.mappers.ItemMapper;
import com.mathsena.stockorderapi.model.Item;
import com.mathsena.stockorderapi.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
  private final ItemRepository itemRepository;
  private final ItemMapper itemMapper;

  private final KafkaProducerService kafkaProducerService;

  private final ObjectMapper mapper;

  public ItemService(
      ItemRepository itemRepository,
      ItemMapper itemMapper,
      KafkaProducerService kafkaProducerService,
      ObjectMapper mapper) {
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
    this.kafkaProducerService = kafkaProducerService;
    this.mapper = mapper;
  }

  public List<ItemDTO> getAllItems() {
    return itemRepository.findAll().stream().map(itemMapper::toDto).collect(Collectors.toList());
  }

  public Optional<ItemDTO> getItemById(Long id) {
    return itemRepository.findById(id).map(itemMapper::toDto);
  }

  public ItemDTO createItem(ItemDTO newItemDTO) {
    Item item = itemMapper.toEntity(newItemDTO);

    try {
      String itemJson = mapper.writeValueAsString(newItemDTO);
      kafkaProducerService.sendMessageItem(itemJson);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return itemMapper.toDto(itemRepository.save(item));
  }

  @Transactional
  public Optional<ItemDTO> updateItem(Long id, ItemDTO updatedItemDTO) {
    return itemRepository
        .findById(id)
        .map(
            item -> {
              itemMapper.setItem(updatedItemDTO, item);
              return itemMapper.toDto(itemRepository.save(item));
            });
  }

  @Transactional
  public boolean deleteItem(Long id) {
    if (itemRepository.existsById(id)) {
      itemRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
