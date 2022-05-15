package br.dev.multicode.api.http.requests;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

  @NotNull
  private UUID userId;

  @NotEmpty
  private Set<ItemOrderRequest> items = new HashSet<>();

  @NotNull
  private BigDecimal price;

}