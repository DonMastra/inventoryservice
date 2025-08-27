package com.orderflow.inventoryservice.events;

import com.orderflow.inventoryservice.commons.InventoryRejectedEvent;
import com.orderflow.inventoryservice.commons.InventoryReservedEvent;
import com.orderflow.inventoryservice.commons.OrderCreatedEvent;
import com.orderflow.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventsHandler {

    private final InventoryService inventoryService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order.created", groupId = "inventory-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        boolean ok = inventoryService.tryReserve(event.productId(), event.quantity());

        if (ok) {
            kafkaTemplate.send("inventory.reserved", String.valueOf(event.orderId()),
                    new InventoryReservedEvent(event.orderId()));
        } else {
            kafkaTemplate.send("inventory.rejected", String.valueOf(event.orderId()),
                    new InventoryRejectedEvent(event.orderId(), "Insufficient stock"));
        }
    }
}
