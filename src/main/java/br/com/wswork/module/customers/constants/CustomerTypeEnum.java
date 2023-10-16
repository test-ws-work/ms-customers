package br.com.wswork.module.customers.constants;

public enum CustomerTypeEnum {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER");

    private final String description;

    CustomerTypeEnum(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
