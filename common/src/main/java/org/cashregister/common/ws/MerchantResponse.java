package org.cashregister.common.ws;
import java.io.Serializable;

/**
 * Created by derkhumblet on 18/03/16.
 */
public class MerchantResponse implements Serializable {
    private String name;
    private boolean truck;
    private String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTruck() {
        return truck;
    }

    public void setTruck(boolean truck) {
        this.truck = truck;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /* Builder */

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MerchantResponse instance = new MerchantResponse();

        public Builder() {}

        public Builder(MerchantResponse employee) {
            this.instance = employee;
        }

        public Builder uuid(String uuid) {
            instance.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            instance.name = name;
            return this;
        }

        public Builder truck(boolean truck) {
            instance.truck = truck;
            return this;
        }

        public MerchantResponse build() {
            MerchantResponse result = instance;
            instance = new MerchantResponse();
            return result;
        }
    }

    /* toString */

    @Override
    public String toString() {
        return "MerchantResponse{" +
                "name='" + name + '\'' +
                "uuid='" + uuid + '\'' +
                "truck='" + truck + '\'' +
                '}';
    }
}
