package com.orderflow.inventoryservice.commons;

public record InventoryRejectedEvent(Long orderId, String reason) {
}
