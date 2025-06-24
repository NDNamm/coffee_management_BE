package com.example.coffee.management.service;

import com.example.coffee.management.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getPayment();
    List<String> getAllPaymentMethods();
}
