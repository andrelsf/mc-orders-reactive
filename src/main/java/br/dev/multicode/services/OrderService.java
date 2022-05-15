package br.dev.multicode.services;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.api.http.responses.OrderResponse;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import java.util.UUID;

public interface OrderService {

  Multi<OrderResponse> findAll();
  Uni<Void> deleteById(UUID orderId);
  Uni<OrderResponse> findById(UUID orderId);
  Uni<OrderResponse> create(OrderRequest orderRequest);

}
