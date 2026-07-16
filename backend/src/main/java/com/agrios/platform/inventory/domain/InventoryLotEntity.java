package com.agrios.platform.inventory.domain;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.*; import java.util.UUID;
@Entity @Table(name="inventory_lot",schema="inventory")
public class InventoryLotEntity {
 @Id private UUID id; @Column(nullable=false) private UUID tenantId; @Column(nullable=false) private UUID inventoryItemId;
 @Column(nullable=false) private String lotReference; @Column(nullable=false) private LocalDate receivedDate; private LocalDate expiryDate;
 @Column(nullable=false) private BigDecimal quantityReceived; @Column(nullable=false) private BigDecimal quantityAvailable;
 private BigDecimal unitCost; @Column(nullable=false) private String currency; private String supplierReference;
 @Column(nullable=false) private Instant createdAt; protected InventoryLotEntity(){}
 public static InventoryLotEntity create(UUID tenantId,UUID itemId,String ref,LocalDate received,LocalDate expiry,BigDecimal q,BigDecimal cost,String currency,String supplier){var v=new InventoryLotEntity();v.id=UUID.randomUUID();v.tenantId=tenantId;v.inventoryItemId=itemId;v.lotReference=ref;v.receivedDate=received;v.expiryDate=expiry;v.quantityReceived=q;v.quantityAvailable=q;v.unitCost=cost;v.currency=currency;v.supplierReference=supplier;v.createdAt=Instant.now();return v;}
 public void consume(BigDecimal q){if(quantityAvailable.compareTo(q)<0)throw new IllegalStateException("Insufficient lot quantity.");quantityAvailable=quantityAvailable.subtract(q);}
 public UUID id(){return id;} public UUID inventoryItemId(){return inventoryItemId;} public String lotReference(){return lotReference;} public LocalDate receivedDate(){return receivedDate;} public LocalDate expiryDate(){return expiryDate;} public BigDecimal quantityAvailable(){return quantityAvailable;} public BigDecimal unitCost(){return unitCost;} public String currency(){return currency;}
}
