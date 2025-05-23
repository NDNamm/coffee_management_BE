package com.example.demo.model.Enum;

public enum PaymentMethod {
    UNPAID("Chưa thanh toán"),
    PAID("Đã thanh toán"),
    PENDING("Đang xử lý"),
    FAILED("Thất bại"),
    REFUNDED("Đã hoàn tiền");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
