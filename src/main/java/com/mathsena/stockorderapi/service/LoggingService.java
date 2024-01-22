package com.mathsena.stockorderapi.service;


import com.mathsena.stockorderapi.model.Order;
import com.mathsena.stockorderapi.model.StockMovement;
import com.mathsena.stockorderapi.model.User;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LoggingService {

  private static final String LOG_FILE_PATH = "logfile.txt";

  public void logOrderCompletion(Order order) {
    String logMessage =
        String.format("[%s] Order completed: ID %d", LocalDateTime.now(), order.getId());
    writeToLogFile(logMessage);
  }

  public void noOrderCompletion(Order order) {
    String logMessage =
            String.format("[%s] Order is not completed: ID %d", LocalDateTime.now(), order.getId());
    writeToLogFile(logMessage);
  }

  public void logStockMovement(StockMovement stockMovement) {
    String logMessage =
        String.format("[%s] Stock Movement: ID %d", LocalDateTime.now(), stockMovement.getId());
    writeToLogFile(logMessage);
  }

  public void logEmailSent(User user, String message) {
    String logMessage = "Email sent successfully to: " + user.getEmail() + "\nMessage: " + message;
    writeToLogFile(logMessage);
  }

  public void logError(String errorMessage) {
    String logMessage = "Error: " + errorMessage;
    writeToLogFile(logMessage);
  }

  private void writeToLogFile(String message) {
    try (FileWriter fileWriter = new FileWriter(LOG_FILE_PATH, true)) {
      message += System.lineSeparator();
      fileWriter.write(message);
      fileWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
