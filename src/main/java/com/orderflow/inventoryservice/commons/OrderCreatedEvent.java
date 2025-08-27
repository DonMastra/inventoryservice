package com.orderflow.inventoryservice.commons;

public record OrderCreatedEvent(Long orderId, Long productId, int quantity) {
}
