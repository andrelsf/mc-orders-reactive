package br.dev.multicode.api.http.responses;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {

  private UUID productId;
  private Integer amount;
  private BigDecimal price;

}