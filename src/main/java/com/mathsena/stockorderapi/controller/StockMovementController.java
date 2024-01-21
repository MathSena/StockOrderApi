package com.mathsena.stockorderapi.controller;

import com.mathsena.stockorderapi.dto.StockMovementDTO;
import com.mathsena.stockorderapi.mappers.StockMovementMapper;
import com.mathsena.stockorderapi.model.StockMovement;
import com.mathsena.stockorderapi.service.StockMovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/stockMovements")
public class StockMovementController {
  private final StockMovementService stockMovementService;
  private final StockMovementMapper stockMovementMapper;

  public StockMovementController(
      StockMovementService stockMovementService, StockMovementMapper stockMovementMapper) {
    this.stockMovementService = stockMovementService;
    this.stockMovementMapper = stockMovementMapper;
  }

  @GetMapping
  public ResponseEntity<List<StockMovement>> getAllStockMovements() {
    List<StockMovement> stockMovements =
            new ArrayList<>(stockMovementService.listAllStockMovements());
    return ResponseEntity.ok(stockMovements);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StockMovement> getStockMovementById(@PathVariable Long id) {
    List<StockMovement> stockMovements = stockMovementService.listStockMovementsByProductId(id);
    if (!stockMovements.isEmpty()) {
      StockMovement stockMovement = stockMovements.get(0);
      return new ResponseEntity<>(stockMovement, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<StockMovementDTO> createStockMovement(
      @RequestBody StockMovementDTO newStockMovementDTO) {
    StockMovement stockMovement = stockMovementService.createStockMovement(newStockMovementDTO);
    StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);
    return new ResponseEntity<>(stockMovementDTO, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<StockMovementDTO> updateStockMovement(
      @PathVariable Long id, @RequestBody StockMovementDTO updatedStockMovementDTO) {
    updatedStockMovementDTO.setId(id);
    StockMovement stockMovement = stockMovementMapper.toEntity(updatedStockMovementDTO);
    StockMovement updatedStockMovement = stockMovementService.updateStockMovement(stockMovement);
    if (updatedStockMovement != null) {
      StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(updatedStockMovement);
      return new ResponseEntity<>(stockMovementDTO, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStockMovement(@PathVariable Long id) {
    stockMovementService.deleteStockMovement(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/item/{itemId}")
  public ResponseEntity<List<StockMovementDTO>> listStockMovementsByProductId(
      @PathVariable Long itemId) {
    List<StockMovementDTO> stockMovementsDTO =
        stockMovementService.listStockMovementsByProductId(itemId).stream()
            .map(stockMovementMapper::toDto)
            .collect(Collectors.toList());
    return new ResponseEntity<>(stockMovementsDTO, HttpStatus.OK);
  }

}
