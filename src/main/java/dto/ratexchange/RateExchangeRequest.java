package dto.ratexchange;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateExchangeRequest {
    @NotBlank(message = "required arg!")
    private String from;

    @NotBlank(message = "required arg!")
    private String to;
}
