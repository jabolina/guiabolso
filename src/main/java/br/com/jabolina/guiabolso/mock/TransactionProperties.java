package br.com.jabolina.guiabolso.mock;

import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;

@Getter
public class TransactionProperties implements MockDataProperties {
    private final Integer copies;
    private final LocalDate start;
    private final Integer months;

    private TransactionProperties(Integer copies, LocalDate start, Integer months) {
        this.copies = copies;
        this.start = start;
        this.months = months;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends MockDataProperties.Builder<TransactionProperties, Builder> {
        private Integer copies;
        private LocalDate start;
        private Integer months;

        public Builder withCopies(Integer copies) {
            this.copies = copies;
            return this;
        }

        public Builder withStart(LocalDate start) {
            this.start = start;
            return this;
        }

        public Builder withMonths(Integer months) {
            this.months = months;
            return this;
        }

        @Override
        public TransactionProperties build() {
            validate();
            return new TransactionProperties(copies, start, months);
        }

        @Override
        void validate() {
            Assert.notNull(copies, "Number of copies can not be null!");
            Assert.isTrue(copies > 0, "Number of copies must be greater than 0!");
            Assert.notNull(start, "Start date can not be null!");
            Assert.isTrue(start.compareTo(LocalDate.now()) < 0, "Start date can not be now!");
            Assert.notNull(months, "Number of months can not be null!");
            Assert.isTrue(months > 0, "Number of months must be greater 0!");
        }
    }
}
