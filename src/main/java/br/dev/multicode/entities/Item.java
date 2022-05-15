package br.dev.multicode.entities;

import br.dev.multicode.api.http.requests.ItemOrderRequest;
import br.dev.multicode.api.http.responses.ItemResponse;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item extends PanacheEntityBase {

  @Id
  @Column(name = "item_id")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @Column(name = "product_id", nullable = false, length = 37)
  private String productId;

  @Column(nullable = false)
  private Integer amount;

  @Column(nullable = false)
  private BigDecimal price;

  public ItemResponse toItemResponse()
  {
    return ItemResponse.builder()
        .productId(UUID.fromString(productId))
        .amount(amount)
        .price(price)
        .build();
  }

  public static Item of(ItemOrderRequest itemOrderRequest)
  {
    return Item.builder()
        .productId(itemOrderRequest.getProductId().toString())
        .amount(itemOrderRequest.getAmount())
        .price(itemOrderRequest.getPrice())
        .build();
  }

  public static Set<Item> of(Set<ItemOrderRequest> itemsFromRequest)
  {
    return itemsFromRequest.stream()
        .map(Item::of)
        .collect(Collectors.toSet());
  }
}
