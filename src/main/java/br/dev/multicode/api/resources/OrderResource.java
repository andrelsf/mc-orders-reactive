package br.dev.multicode.api.resources;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.services.OrderService;
import io.smallrye.mutiny.Uni;
import java.net.URI;
import java.util.UUID;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

  @Inject
  OrderService orderService;

  @POST
  public Uni<Response> postOrder(@Valid OrderRequest orderRequest)
  {
     return orderService.create(orderRequest)
       .onItem()
       .transform(orderResponse -> Response
         .created(URI.create("/api/orders/".concat(orderResponse.getOrderId())))
         .header("orderId", orderResponse.getOrderId())
         .build());
  }

  @GET
  @Path("/{orderId}")
  public Uni<Response> getSingleOrderById(@PathParam("orderId") UUID orderId)
  {
    return orderService.findById(orderId)
        .onItem()
        .transform(orderResponse -> Response.ok(orderResponse).build());
  }
}
