package com.agrios.platform.inventory.api;
import com.agrios.platform.inventory.domain.*; import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.time.LocalDate; import java.util.UUID;
public final class InventoryDtos {private InventoryDtos(){}
 public record CreateItemRequest(@NotNull UUID farmId,@NotBlank String itemCode,@NotBlank String itemName,@NotBlank String category,@NotBlank String baseUnit,@PositiveOrZero BigDecimal reorderLevel){}
 public record ReceiveRequest(@NotBlank String lotReference,@NotNull LocalDate receivedDate,LocalDate expiryDate,@NotNull @Positive BigDecimal quantity,@PositiveOrZero BigDecimal unitCost,@NotBlank String currency,String supplierReference,String notes){}
 public record AdjustRequest(@NotNull @Positive BigDecimal quantity,@NotBlank String direction,@NotBlank String reason,String idempotencyKey){}
 public record ItemResponse(UUID id,UUID farmId,String itemCode,String itemName,String category,String baseUnit,BigDecimal reorderLevel,BigDecimal currentQuantity,boolean lowStock,boolean active,long version){public static ItemResponse from(InventoryItemEntity v){return new ItemResponse(v.id(),v.farmId(),v.itemCode(),v.itemName(),v.category(),v.baseUnit(),v.reorderLevel(),v.currentQuantity(),v.lowStock(),v.active(),v.version());}}
 public record LotResponse(UUID id,String lotReference,LocalDate receivedDate,LocalDate expiryDate,BigDecimal quantityAvailable,BigDecimal unitCost,String currency){public static LotResponse from(InventoryLotEntity v){return new LotResponse(v.id(),v.lotReference(),v.receivedDate(),v.expiryDate(),v.quantityAvailable(),v.unitCost(),v.currency());}}
}
