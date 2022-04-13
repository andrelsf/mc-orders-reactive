package br.dev.multicode.services.impl;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.api.http.responses.OrderResponse;
import br.dev.multicode.entities.Order;
import br.dev.multicode.repositories.OrderRepository;
import br.dev.multicode.services.OrderService;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

  @Inject
  OrderRepository orderRepository;

  @Override
  public Multi<OrderResponse> findAll()
  {
    return orderRepository.streamAll()
      .map(Order::toResponse);
  }

  @Override
  @ReactiveTransactional
  public Uni<Void> deleteById(final UUID orderId)
  {
    return this.findOrderById(orderId.toString())
      .call(orderRepository::delete)
      .replaceWithVoid();
  }

  @Override
  public Uni<OrderResponse> findById(final UUID orderId)
  {
    return this.findOrderById(orderId.toString())
      .map(Order::toResponse);
  }

  @Override
  @ReactiveTransactional
  public Uni<OrderResponse> create(final OrderRequest orderRequest)
  {
    final Order order = Order.of(orderRequest);
    return orderRepository.persistAndFlush(order)
      .map(Order::toResponse);
  }

  private Uni<Order> findOrderById(String orderId)
  {
    return orderRepository.find(orderId)
      .replaceIfNullWith(() -> {
        throw new IllegalArgumentException("Order not found by ID=".concat(orderId));
      });
  }
}
