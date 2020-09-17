package br.com.jabolina.guiabolso.data.domain;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class User implements Serializable {
    @NotNull
    @Min(1_000)
    @Max(100_000_000)
    private Integer id;

    @NotNull
    private List<Transaction> transactions;
}
