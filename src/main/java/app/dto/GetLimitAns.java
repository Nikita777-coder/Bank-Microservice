package app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetLimitAns {
    private List<TransactionDTO> body;
    public static GetLimitAns of(List<TransactionDTO> body) {
        GetLimitAns obj = new GetLimitAns();
        obj.body = body;

        return obj;
    }
}
