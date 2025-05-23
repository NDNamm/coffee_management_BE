package com.example.demo.model.Enum;

public enum ProductStatus {
    AVAILABLE("Đang bán"),
    OUT_OF_STOCK("Hết hàng"),
    DISCONTINUED("Ngừng kinh doanh");

    private final String label;

    ProductStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

