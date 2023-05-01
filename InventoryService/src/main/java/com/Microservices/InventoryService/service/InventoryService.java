package com.Microservices.InventoryService.service;

import com.Microservices.InventoryService.dto.InventoryResponse;
import com.Microservices.InventoryService.model.Inventory;
import com.Microservices.InventoryService.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        List<Inventory> inventories = inventoryRepository.findAllBySkuCodeIn(skuCodes);

        return inventories.stream().map(inventory -> InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build()).toList();
    }
}
