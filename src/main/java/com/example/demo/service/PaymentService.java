package com.example.demo.service;

import com.example.demo.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getPayment();
    List<String> getAllPaymentMethods();
}
