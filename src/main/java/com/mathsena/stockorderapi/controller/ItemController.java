package com.mathsena.stockorderapi.controller;

import com.mathsena.stockorderapi.dto.ItemDTO;
import com.mathsena.stockorderapi.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/items")
public class ItemController {
  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping
  public ResponseEntity<List<ItemDTO>> getAllItems() {
    return ResponseEntity.ok(itemService.getAllItems());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
    return itemService.getItemById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO newItem) {
    ItemDTO createdItem = itemService.createItem(newItem);
    return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @RequestBody ItemDTO updatedItem) {
    return itemService.updateItem(id, updatedItem)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
    return itemService.deleteItem(id) ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
  }
}
