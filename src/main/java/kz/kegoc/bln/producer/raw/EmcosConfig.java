package kz.kegoc.bln.producer.raw;

import lombok.Getter;

public class EmcosConfig {
    @Getter private final String url;
    @Getter private final String user;
    @Getter private final String func;
    @Getter private final Boolean isPacked;
    @Getter private final String attType;

    private EmcosConfig(Builder builder) {
        url = builder.url;
        user = builder.user;
        func = builder.func;
        isPacked = builder.isPacked;
        attType = builder.attType;
    }

    public static Builder defaultEmcosServer() {
        return new Builder()
            .url("http://10.8.144.11/STWS2/STExchangeWS2.dll/soap/IST_ExchWebService2")
            .user("yug")
            .func("REQML")
            .isPacked(false)
            .attType("1");
    }

    public static class Builder {
        private String url;
        private String user;
        private String func;
        private Boolean isPacked;
        private String attType;

        public Builder url(final String url) {
            this.url = url;
            return this;
        }

        public Builder user(final String user) {
            this.user = user;
            return this;
        }

        public Builder func(final String func) {
            this.func = func;
            return this;
        }

        public Builder isPacked(final Boolean isPacked) {
            this.isPacked = isPacked;
            return this;
        }

        public Builder attType(final String attType) {
            this.attType = attType;
            return this;
        }

        public EmcosConfig build() {
            return new EmcosConfig(this);
        }
    }
}
