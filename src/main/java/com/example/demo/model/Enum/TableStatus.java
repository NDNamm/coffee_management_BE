package com.example.demo.model.Enum;


public enum TableStatus {
    AVAILABLE("Bàn trống"),
    OCCUPIED("Đang có khách"),
    RESERVED("Đã được đặt trước"),
    CLEANING("Đang dọn dẹp"),
    OUT_OF_ORDER("Không sử dụng được");

    private final String label;

    TableStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
