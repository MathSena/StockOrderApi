package com.mathsena.stockorderapi.controller;

import com.mathsena.stockorderapi.dto.ItemDTO;
import com.mathsena.stockorderapi.service.ItemService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/items")
@Api(tags = "Item Controller", description = "Endpoints for managing items")
public class ItemController {
  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping
  @ApiOperation("Get a list of all items")
  @ApiResponse(code = 200, message = "List of items retrieved successfully")
  public ResponseEntity<List<ItemDTO>> getAllItems() {
    List<ItemDTO> items = itemService.getAllItems();
    return ResponseEntity.ok(items);
  }

  @GetMapping("/{id}")
  @ApiOperation("Get an item by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Item retrieved successfully"),
          @ApiResponse(code = 404, message = "Item not found")
  })
  public ResponseEntity<Object> getItemById(@PathVariable Long id) {
    Optional<ItemDTO> itemOptional = itemService.getItemById(id);
    return itemOptional
            .<ResponseEntity<Object>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found!"));
  }

  @PostMapping
  @ApiOperation("Create a new item")
  @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Item created successfully"),
          @ApiResponse(code = 400, message = "Invalid request body")
  })
  public ResponseEntity<Object> createItem(@RequestBody @Valid ItemDTO newItem) {
    ItemDTO createdItem = itemService.createItem(newItem);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
  }

  @PutMapping("/{id}")
  @ApiOperation("Update an item by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Item updated successfully"),
          @ApiResponse(code = 400, message = "Invalid request body"),
          @ApiResponse(code = 404, message = "Item not found")
  })
  public ResponseEntity<Object> updateItem(
          @PathVariable Long id, @RequestBody @Valid ItemDTO updatedItem) {
    Optional<ItemDTO> updatedItemOptional = itemService.updateItem(id, updatedItem);
    return updatedItemOptional
            .<ResponseEntity<Object>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found!"));
  }

  @DeleteMapping("/{id}")
  @ApiOperation("Delete an item by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 204, message = "Item deleted successfully"),
          @ApiResponse(code = 404, message = "Item not found")
  })
  public ResponseEntity<Object> deleteItem(@PathVariable Long id) {
    boolean deleted = itemService.deleteItem(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found!");
    }
  }
}
