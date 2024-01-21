package com.mathsena.stockorderapi.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetails {
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;

  public ErrorDetails(
      LocalDateTime timestamp, int status, String error, String message, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }
}
