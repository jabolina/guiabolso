package br.com.jabolina.guiabolso.data.domain;

import br.com.jabolina.guiabolso.util.annotation.RandomData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode
public class Transaction implements Serializable {
    @NotNull
    @NotBlank
    @Length(min = 10, max = 60)
    @RandomData(min = 10, max = 60)
    private String description;

    @NotNull
    @Min(-9_999_999)
    @Max(9_999_999)
    @RandomData(min = -9_999_999, max = 9_999_999, type = RandomData.Type.NUMBER)
    private Long value;

    @RandomData(type = RandomData.Type.DATE)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date date;

    @EqualsAndHashCode.Exclude
    private Boolean duplicated;
}
