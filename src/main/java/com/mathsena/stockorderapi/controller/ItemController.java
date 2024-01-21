package com.mathsena.stockorderapi.controller;

import com.mathsena.stockorderapi.dto.ItemDTO;
import com.mathsena.stockorderapi.mappers.ItemMapper;
import com.mathsena.stockorderapi.model.Item;
import com.mathsena.stockorderapi.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/items")
public class ItemController {
  private final ItemService itemService;

  private final ItemMapper itemMapper;

  public ItemController(ItemService itemService, ItemMapper itemMapper) {
    this.itemService = itemService;
    this.itemMapper = itemMapper;
  }

  @GetMapping
  public ResponseEntity<List<ItemDTO>> getAllItems() {
    return ResponseEntity.ok(itemService.getAllItems());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> getItemById(@PathVariable Long id) {
    Item item = itemService.getItemById(id);
    if (item != null) {
      return ResponseEntity.ok(item);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO newItem) {
    Item item = itemService.createItem(newItem);
    ItemDTO itemDTO = itemMapper.toDto(item);
    return ResponseEntity.ok(itemDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Item> updateItem(
      @PathVariable Long id, @RequestBody ItemDTO updatedItem) {
    Item item = itemService.updateItem(id, updatedItem);
    if (item != null) {
      return ResponseEntity.ok(item);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
    if (itemService.deleteItem(id)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
