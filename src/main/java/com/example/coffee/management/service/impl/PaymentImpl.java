package com.example.coffee.management.service.impl;

import com.example.coffee.management.dto.PaymentDTO;
import com.example.coffee.management.model.enums.PaymentMethod;
import com.example.coffee.management.model.Payment;
import com.example.coffee.management.repository.PaymentRepository;
import com.example.coffee.management.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<PaymentDTO> getPayment() {
        List<Payment> payment =paymentRepository.findAll();

        return payment.stream().map(method -> {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(method.getId());
            paymentDTO.setAmount(method.getAmount());
            paymentDTO.setPaymentMethod(method.getPaymentMethod());
            return paymentDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllPaymentMethods() {
        return Arrays.stream(PaymentMethod.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
