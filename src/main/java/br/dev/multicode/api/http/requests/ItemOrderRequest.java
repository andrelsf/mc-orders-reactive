package br.dev.multicode.api.http.requests;

import java.math.BigDecimal;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrderRequest {

  @NotNull
  private UUID productId;

  @Positive
  private Integer amount;

  @NotNull
  private BigDecimal price;

}
