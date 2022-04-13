package br.dev.multicode.entities;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.api.http.responses.OrderResponse;
import br.dev.multicode.enums.OrderStatus;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET status = 'CANCELED' WHERE order_id = ?")
@Where(clause = "status != 'CANCELED'")
public class Order extends PanacheEntityBase {

  @Id
  @Column(name = "order_id", length = 37)
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @Column(name = "user_id", length = 37, nullable = false)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(length = 30, nullable = false)
  private OrderStatus status;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "orders_items",
    joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id"))
  private Set<Item> items = new HashSet<>();

  @Column(nullable = false)
  private BigDecimal price;

  @CreationTimestamp
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @PrePersist
  private void prePersist()
  {
    this.id = UUID.randomUUID().toString();
  }

  public static Order of(OrderRequest orderRequest)
  {
    return Order.builder()
        .userId(orderRequest.getUserId().toString())
        .status(OrderStatus.CREATED)
        .items(Item.of(orderRequest.getItems()))
        .price(orderRequest.getPrice())
        .build();
  }

  public OrderResponse toResponse()
  {
    return OrderResponse.builder()
        .orderId(id)
        .userId(userId)
        .status(status.name())
        .items(items.stream()
            .map(Item::toItemResponse)
            .collect(Collectors.toSet()))
        .price(price.toString())
        .build();
  }
}
