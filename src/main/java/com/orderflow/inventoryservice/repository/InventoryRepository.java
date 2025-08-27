package com.orderflow.inventoryservice.repository;

import com.orderflow.inventoryservice.domain.InventoryItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryRepository {

    @PersistenceContext
    private EntityManager em;

    public InventoryItem findByProductIdForUpdate(Long productId) {
        return em.createQuery("SELECT i FROM inventory_item i WHERE i.productId = :pid", InventoryItem.class)
                .setParameter("pid", productId)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void save(InventoryItem item) {
        if (item.getId() == null) em.persist(item);
        else em.merge(item);
    }
}
