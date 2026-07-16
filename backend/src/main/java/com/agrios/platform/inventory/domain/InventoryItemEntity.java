package com.agrios.platform.inventory.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory_item", schema = "inventory")
public class InventoryItemEntity {
    @Id private UUID id;
    @Column(nullable = false) private UUID tenantId;
    @Column(nullable = false) private UUID farmId;
    @Column(nullable = false) private String itemCode;
    @Column(nullable = false) private String itemName;
    @Column(nullable = false) private String category;
    @Column(nullable = false) private String baseUnit;
    private BigDecimal reorderLevel;
    @Column(nullable = false) private BigDecimal currentQuantity;
    @Column(nullable = false) private boolean active;
    @Version private long version;
    @Column(nullable = false) private Instant createdAt;
    private UUID createdBy;
    @Column(nullable = false) private Instant updatedAt;
    private UUID updatedBy;

    protected InventoryItemEntity() {}
    public static InventoryItemEntity create(UUID tenantId, UUID farmId, String code, String name,
            String category, String unit, BigDecimal reorderLevel, UUID actorId) {
        var v = new InventoryItemEntity();
        v.id=UUID.randomUUID(); v.tenantId=tenantId; v.farmId=farmId; v.itemCode=code;
        v.itemName=name; v.category=category; v.baseUnit=unit; v.reorderLevel=reorderLevel;
        v.currentQuantity=BigDecimal.ZERO; v.active=true; v.createdAt=Instant.now();
        v.updatedAt=v.createdAt; v.createdBy=actorId; v.updatedBy=actorId; return v;
    }
    public void increase(BigDecimal q, UUID actorId) { currentQuantity=currentQuantity.add(q); touch(actorId); }
    public void decrease(BigDecimal q, UUID actorId) {
        if (currentQuantity.compareTo(q)<0) throw new IllegalStateException("Insufficient inventory quantity.");
        currentQuantity=currentQuantity.subtract(q); touch(actorId);
    }
    private void touch(UUID actorId){updatedAt=Instant.now();updatedBy=actorId;}
    public UUID id(){return id;} public UUID farmId(){return farmId;} public String itemCode(){return itemCode;}
    public String itemName(){return itemName;} public String category(){return category;} public String baseUnit(){return baseUnit;}
    public BigDecimal reorderLevel(){return reorderLevel;} public BigDecimal currentQuantity(){return currentQuantity;}
    public boolean active(){return active;} public long version(){return version;}
    public boolean lowStock(){return reorderLevel!=null && currentQuantity.compareTo(reorderLevel)<=0;}
}
