package com.mathsena.stockorderapi.controller;

import com.mathsena.stockorderapi.dto.StockMovementDTO;
import com.mathsena.stockorderapi.mappers.StockMovementMapper;
import com.mathsena.stockorderapi.model.StockMovement;
import com.mathsena.stockorderapi.service.StockMovementService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/api/stockMovements")
@Api(tags = "Stock Movement Controller", description = "Endpoints for managing stock movements")
public class StockMovementController {
  private final StockMovementService stockMovementService;
  private final StockMovementMapper stockMovementMapper;

  public StockMovementController(
          StockMovementService stockMovementService, StockMovementMapper stockMovementMapper) {
    this.stockMovementService = stockMovementService;
    this.stockMovementMapper = stockMovementMapper;
  }

  @GetMapping
  @ApiOperation("Get a list of all stock movements")
  @ApiResponse(code = 200, message = "List of stock movements retrieved successfully")
  public ResponseEntity<List<StockMovement>> getAllStockMovements() {
    List<StockMovement> stockMovements = stockMovementService.listAllStockMovements();
    return ResponseEntity.ok(stockMovements);
  }

  @GetMapping("/{id}")
  @ApiOperation("Get a stock movement by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Stock movement retrieved successfully"),
          @ApiResponse(code = 404, message = "Stock movement not found")
  })
  public ResponseEntity<Object> getStockMovementById(@PathVariable Long id) {
    List<StockMovement> stockMovements = stockMovementService.listStockMovementsByProductId(id);
    if (!stockMovements.isEmpty()) {
      StockMovement stockMovement = stockMovements.get(0);
      return ResponseEntity.ok(stockMovement);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock movement not found!");
    }
  }

  @PostMapping
  @ApiOperation("Create a new stock movement")
  @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Stock movement created successfully"),
          @ApiResponse(code = 400, message = "Invalid request body")
  })
  public ResponseEntity<Object> createStockMovement(@RequestBody StockMovementDTO newStockMovementDTO) {
    StockMovement stockMovement = stockMovementService.createStockMovement(newStockMovementDTO);
    StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);
    return ResponseEntity.status(HttpStatus.CREATED).body(stockMovementDTO);
  }

  @PutMapping("/{id}")
  @ApiOperation("Update a stock movement by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Stock movement updated successfully"),
          @ApiResponse(code = 400, message = "Invalid request body"),
          @ApiResponse(code = 404, message = "Stock movement not found")
  })
  public ResponseEntity<Object> updateStockMovement(
          @PathVariable Long id, @RequestBody StockMovementDTO updatedStockMovementDTO) {
    updatedStockMovementDTO.setId(id);
    StockMovement stockMovement = stockMovementMapper.toEntity(updatedStockMovementDTO);
    StockMovement updatedStockMovement = stockMovementService.updateStockMovement(stockMovement);
    if (updatedStockMovement != null) {
      StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(updatedStockMovement);
      return ResponseEntity.ok(stockMovementDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock movement not found!");
    }
  }

  @DeleteMapping("/{id}")
  @ApiOperation("Delete a stock movement by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 204, message = "Stock movement deleted successfully"),
          @ApiResponse(code = 404, message = "Stock movement not found")
  })
  public ResponseEntity<Void> deleteStockMovement(@PathVariable Long id) {
    stockMovementService.deleteStockMovement(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/item/{itemId}")
  @ApiOperation("List stock movements by product ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "List of stock movements retrieved successfully"),
          @ApiResponse(code = 404, message = "No stock movements found for the given product ID")
  })
  public ResponseEntity<Object> listStockMovementsByProductId(@PathVariable Long itemId) {
    List<StockMovementDTO> stockMovementsDTO =
            stockMovementService.listStockMovementsByProductId(itemId).stream()
                    .map(stockMovementMapper::toDto)
                    .collect(Collectors.toList());
    if (!stockMovementsDTO.isEmpty()) {
      return ResponseEntity.ok(stockMovementsDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock movements found for the given product ID");
    }
  }
}
