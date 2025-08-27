package com.orderflow.inventoryservice.service.impl;

import com.orderflow.inventoryservice.domain.InventoryItem;
import com.orderflow.inventoryservice.repository.InventoryRepository;
import com.orderflow.inventoryservice.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public boolean tryReserve(Long productId, int qty) {
        InventoryItem item = inventoryRepository.findByProductIdForUpdate(productId); // haz SELECT ... FOR UPDATE en JPA/Native si usás DB
        if (item == null || item.getQuantity() < qty) return false;
        item.setQuantity(item.getQuantity() - qty);
        inventoryRepository.save(item);
        return true;
    }
}
