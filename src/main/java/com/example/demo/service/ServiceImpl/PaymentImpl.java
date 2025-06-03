package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.PaymentDTO;
import com.example.demo.model.Enum.PaymentMethod;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;
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
