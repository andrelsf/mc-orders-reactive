package br.dev.multicode.api.http.responses;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

  private String orderId;
  private String userId;
  private String status;
  private Set<ItemResponse> items = new HashSet<>();
  private String price;

}