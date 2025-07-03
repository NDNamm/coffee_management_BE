package com.example.coffee.management.service.impl;

import com.example.coffee.management.dto.AddressDTO;
import com.example.coffee.management.dto.ItemDTO;
import com.example.coffee.management.dto.OrderDTO;
import com.example.coffee.management.exception.AppException;
import com.example.coffee.management.exception.ErrorCode;
import com.example.coffee.management.model.*;
import com.example.coffee.management.model.enums.OrderStatus;
import com.example.coffee.management.model.enums.PaymentMethod;
import com.example.coffee.management.model.enums.ProductStatus;
import com.example.coffee.management.dto.OrderDetailDTO;
import com.example.coffee.management.repository.*;
import com.example.coffee.management.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;


    //Admin
    @Override
    public Page<OrderDTO> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Order> orders = orderRepository.findAll(pageable);


        return orders.map(order -> OrderDTO.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .note(order.getNote())
                .addressDTO(order.getAddress() != null ? mapToAddressDTO(order.getAddress()) : null)
                .orderDetailDTO(getOrderDetailDTOS(order))
                .paymentMethod(order.getPayment().getPaymentMethod())
                .orderDate(order.getOrderDate())
                .build());

    }
    private AddressDTO mapToAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .receiverName(address.getReceiverName())
                .phoneNumber(address.getPhoneNumber())
                .homeAddress(address.getHomeAddress())
                .city(address.getCity())
                .district(address.getDistrict())
                .commune(address.getCommune())
                .createdAt(address.getCreatedAt())
                .isDefault(address.isDefault())
                .build();
    }


    @Override
    public void updateOrder(OrderDTO orderDTO, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setStatus(orderDTO.getStatus());
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        orderRepository.delete(order);
    }

    @Override
    public Page<OrderDTO> searchOrderByKey(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        Page<Order> orders = orderRepository.searchOrderByNameOrUserFullName(key, pageable);

        return orders.map(order -> OrderDTO.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPayment() != null ? order.getPayment().getPaymentMethod() : null)
                .build()
        );
    }

    //Client
    @Override
    @Transactional
    public void addOrder(OrderDTO orderDTO, String email, String sessionId) {
        Users user = null;
        Carts cart;

        if (email != null && !email.equals("anonymousUser")) {
            user = userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            cart = cartRepository.findByUser(user);
        } else if (sessionId != null) {
            cart = cartRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new AppException(ErrorCode.SESSION_NOT_FOUND));
        } else {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }


        Order order = new Order();
        order.setUser(user);
        order.setSessionId(user == null ? sessionId : null);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setNote(orderDTO.getNote());

        BigDecimal totalAmount = cart.getItems().stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        List<OrderDetail> orderDetails = buildOrderDetails(order, cart, orderDTO.getItems());
        order.setOrderDetail(orderDetails);

        orderRepository.save(order);

        String receiverName = user != null ? user.getFullName() : orderDTO.getAddressDTO().getReceiverName();

        Address address = Address.builder()
                .receiverName(receiverName)
                .city(orderDTO.getAddressDTO().getCity())
                .district(orderDTO.getAddressDTO().getDistrict())
                .commune(orderDTO.getAddressDTO().getCommune())
                .homeAddress(orderDTO.getAddressDTO().getHomeAddress())
                .phoneNumber(orderDTO.getAddressDTO().getPhoneNumber())
                .order(order)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        if (user != null) {
            address.setUser(user);
        }

        addressRepository.save(address);
        order.setAddress(address);


        Payment payment = new Payment();
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentDate(LocalDate.now());

        PaymentMethod method = orderDTO.getPaymentMethod();
        if (method != null) {
            payment.setPaymentMethod(method);
        } else {
            throw new AppException(ErrorCode.NOT_PAYMENT_METHOD);
        }

        payment.setOrder(order);
        paymentRepository.save(payment);

    }

    @Override
    public Page<OrderDTO> getOrderClient(String email, String sessionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Order> orders = orderRepository.findAll(pageable);

        if (email != null && sessionId != null) {
            orders = orderRepository.findOrdersByUserEmailOrSessionId(email, sessionId, pageable);
        } else if (email != null) {
            orders = orderRepository.findOrdersByUserEmail(email, pageable);
        } else if (sessionId != null) {
            orders = orderRepository.findOrdersBySessionId(sessionId, pageable);
        }

        return orders.map(order -> OrderDTO.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .paymentMethod(order.getPayment().getPaymentMethod())
                .orderDetailDTO(getOrderDetailDTOS(order))
                .build());

    }

    @Override
    public void updateOrderByClient(OrderDTO orderDTO, Long orderId, String email, String sessionId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        validateOrderOwnership(order, email, sessionId);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_MODIFIED);
        }

        updateOrderInfo(order, orderDTO);
        updateOrderAddress(order, orderDTO);

        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long orderId, String email, String sessionId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        validateOrderOwnership(order, email, sessionId);
        if (order.getStatus() != OrderStatus.PENDING)
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_MODIFIED);

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    private void validateOrderOwnership(Order order, String email, String sessionId) {
        if (email != null && !email.equals("anonymousUser")) {
            Users user = userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            boolean isOwner = order.getUser() != null && order.getUser().getId().equals(user.getId());
            if (!isOwner) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

        } else if (sessionId != null) {
            boolean isSessionMatch = sessionId.equals(order.getSessionId());
            if (!isSessionMatch) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    private void updateOrderInfo(Order order, OrderDTO dto) {
        if (dto.getNote() != null) {
            order.setNote(dto.getNote());
        }
    }

    private void updateOrderAddress(Order order, OrderDTO dto) {
        if (dto.getAddressDTO() == null || order.getAddress() == null) return;

        AddressDTO source = dto.getAddressDTO();
        Address target = order.getAddress();

        target.setReceiverName(source.getReceiverName());
        target.setPhoneNumber(source.getPhoneNumber());
        target.setHomeAddress(source.getHomeAddress());
        target.setCity(source.getCity());
        target.setDistrict(source.getDistrict());
        target.setCommune(source.getCommune());
        target.setUpdateAt(LocalDateTime.now());

        addressRepository.save(target);
    }

    private List<OrderDetail> buildOrderDetails(Order order, Carts cart, List<ItemDTO> itemDTOs) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        if (cart != null && !cart.getItems().isEmpty()) {
            for (CartItems cartItem : cart.getItems()) {
                Product product = productRepository.findById(cartItem.getProduct().getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                if (!product.getStatus().equals(ProductStatus.AVAILABLE)) {
                    throw new AppException(ErrorCode.PRODUCT_OUT_OF_STOCK);
                }

                OrderDetail orderDetail = OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPrice())
                        .totalPrice(cartItem.getTotalPrice())
                        .build();

                orderDetails.add(orderDetail);
            }

        } else if (itemDTOs != null && !itemDTOs.isEmpty()) {
            for (ItemDTO dto : itemDTOs) {
                Product product = productRepository.findById(dto.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                if (!product.getStatus().equals(ProductStatus.AVAILABLE)) {
                    throw new AppException(ErrorCode.PRODUCT_OUT_OF_STOCK);
                }

                OrderDetail orderDetail = OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .quantity(dto.getQuantity())
                        .price(dto.getPrice())
                        .totalPrice(dto.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())))
                        .build();

                orderDetails.add(orderDetail);
            }
        } else {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        return orderDetails;
    }

    //Ham chung
    private static List<OrderDetailDTO> getOrderDetailDTOS(Order order) {
        List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        if (order.getOrderDetail() != null) {
            for (OrderDetail items : order.getOrderDetail()) {
                OrderDetailDTO dto = OrderDetailDTO.builder()
                        .id(items.getId())
                        .urlProductImage(items.getProduct().getImageUrl())
                        .productName(items.getProduct().getNamePro())
                        .quantity(items.getQuantity())
                        .price(items.getPrice())
                        .totalPrice(items.getTotalPrice())
                        .build();
                orderDetailDTOS.add(dto);
            }
        }
        return orderDetailDTOS;
    }

}
