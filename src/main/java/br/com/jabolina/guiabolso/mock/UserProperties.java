package br.com.jabolina.guiabolso.mock;

import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class UserProperties implements MockDataProperties {
    private final Integer id;

    private UserProperties(Integer id) {
        this.id = id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends MockDataProperties.Builder<UserProperties, Builder> {
        private Integer id;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        @Override
        void validate() {
            Assert.notNull(id, "User ID should not be null!");
            Assert.isTrue(id >= 1_000, "User ID should be greater!");
            Assert.isTrue(id <= 100_000_000, "User ID should be smaller!");
        }

        @Override
        public UserProperties build() {
            validate();
            return new UserProperties(id);
        }
    }
}
