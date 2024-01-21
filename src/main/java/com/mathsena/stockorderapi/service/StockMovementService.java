package com.mathsena.stockorderapi.service;

import com.mathsena.stockorderapi.dto.StockMovementDTO;
import com.mathsena.stockorderapi.model.StockMovement;

import java.util.List;

public interface StockMovementService {
  public StockMovement createStockMovement(StockMovementDTO stockMovement);

  public List<StockMovement> listAllStockMovements();

  public List<StockMovement> listStockMovementsByProductId(Long itemId);

  public StockMovement updateStockMovement(StockMovement stockMovement);

  public boolean deleteStockMovement(Long id);

}
