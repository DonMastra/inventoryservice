package com.orderflow.inventoryservice.service;

public interface InventoryService {
    boolean tryReserve(Long productId, int qty);
}
