package br.dev.multicode.repositories;

import br.dev.multicode.entities.Order;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;

public interface OrderRepository extends PanacheRepository<Order> {

  default Uni<Order> find(String orderId) {
    return this.find("order_id = :orderId", Parameters.with("orderId", orderId))
        .firstResult();
  }

}
