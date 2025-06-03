package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.model.*;
import com.example.demo.model.Enum.OrderStatus;
import com.example.demo.model.Enum.PaymentMethod;
import com.example.demo.model.Enum.ProductStatus;
import com.example.demo.model.Enum.TableStatus;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Page<OrderDTO> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Order> orders = orderRepository.findAll(pageable);

        return orders.map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setName(order.getName());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());
            dto.setNote(order.getNote());
            List<OrderDetailDTO> orderDetailsDTO = new ArrayList<>();
            if (order.getOrderDetail() != null) {
                orderDetailsDTO = order.getOrderDetail().stream().map(
                        orderDetail -> {
                            OrderDetailDTO detailDTO = new OrderDetailDTO();
                            detailDTO.setId(orderDetail.getId());
                            detailDTO.setProductName(orderDetail.getProduct().getName());
                            detailDTO.setPrice(orderDetail.getPrice());
                            detailDTO.setQuantity(orderDetail.getQuantity());
                            return detailDTO;
                        }
                ).collect(Collectors.toList());
            } else {
                throw new RuntimeException("Chưa có đơn hàng");
            }
            dto.setOrderDetailDTO(orderDetailsDTO);
            dto.setOrderDate(order.getOrderDate());
            if (order.getPayment() != null) {
                dto.setPaymentMethod(order.getPayment().getPaymentMethod());
            }

            if (order.getUser() != null) {
                dto.setUserId(order.getUser().getId());
            } else {
                dto.setSessionId(order.getSessionId());
            }
            return dto;
        });
    }

    @Override
    @Transactional
    public void addOrder(OrderDTO orderDTO, String email, String sessionId) {
        Users user = null;
        Carts cart = null;

        if (email != null && !email.equals("anonymousUser")) {
            user = userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            cart = cartRepository.findByUser(user);
        } else if (sessionId != null) {
            cart = cartRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng session"));
        } else {
            throw new RuntimeException("Thiếu thông tin đăng nhập hoặc session");
        }

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng rỗng");
        }


        Order order = new Order();
        order.setName(user != null ? user.getFullName() : orderDTO.getName());
        order.setUser(user);
        order.setSessionId(user == null ? sessionId : null);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setNote(orderDTO.getNote());

        BigDecimal totalAmount = BigDecimal.ZERO;
//        if (user != null) {
        totalAmount = cart.getItems().stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        } else {
//            if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
//                throw new RuntimeException("Danh sách sản phẩm đặt hàng không được để trống");
//            }
//            totalAmount = cart.getItems().stream()
//                    .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//        }

        order.setTotalAmount(totalAmount);

        List<OrderDetail> orderDetails = buildOrderDetails(order, cart, orderDTO.getItems());
        order.setOrderDetail(orderDetails);

        if (!orderDTO.isTakeAway()) {
            if (orderDTO.getTableId() == null) {
                throw new RuntimeException("Phải chọn bàn nếu không phải takeaway");
            }
            Tables tables = tableRepository.findById(orderDTO.getTableId())
                    .orElseThrow(() -> new RuntimeException("Bàn không tồn tại"));
            if (!tables.getStatus().equals(TableStatus.AVAILABLE)) {
                throw new RuntimeException("Bàn đã được sử dụng");
            }
            tables.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(tables);

            order.setTable(tables);
        }

        orderRepository.save(order);

        Payment payment = new Payment();
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(orderDTO.getPaymentMethod());
        payment.setOrder(order);
        paymentRepository.save(payment);


    }

    @Override
    public List<OrderDTO> getOrder(String email, String sessionId) {
        List<Order> orders = new ArrayList<>();

        if (email != null && sessionId != null) {
            orders = orderRepository.findOrdersByUserEmailOrSessionId(email, sessionId);
        } else if (email != null) {
            orders = orderRepository.findOrdersByUserEmail(email);
        } else if (sessionId != null) {
            orders = orderRepository.findOrdersBySessionId(sessionId);
        }

        orders.sort(Comparator.comparing(Order::getId).reversed());

        return orders.stream().map(order -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setName(order.getName());
            orderDTO.setTotalAmount(order.getTotalAmount());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setNote(order.getNote());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setPaymentMethod(order.getPayment().getPaymentMethod());

            List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
            if (order.getOrderDetail() != null) {
                for (OrderDetail items : order.getOrderDetail()) {
                    OrderDetailDTO dto = new OrderDetailDTO();
                    dto.setId(items.getId());
                    dto.setUrlProductImage(items.getProduct().getImageUrl());
                    dto.setProductName(items.getProduct().getName());
                    dto.setQuantity(items.getQuantity());
                    dto.setPrice(items.getPrice());
                    dto.setTotalPrice(items.getTotalPrice());
                    orderDetailDTOS.add(dto);
                }
            }
            orderDTO.setOrderDetailDTO(orderDetailDTOS);
            return orderDTO;
        }).collect(Collectors.toList());
    }


    private List<OrderDetail> buildOrderDetails(Order order, Carts cart, List<ItemDTO> itemDTOs) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        if (cart != null && !cart.getItems().isEmpty() && cart.getItems() != null) {
            for (CartItems cartItem : cart.getItems()) {
                Product product = productRepository.findById(cartItem.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

                if (!product.getStatus().equals(ProductStatus.AVAILABLE)) {
                    throw new RuntimeException("Sản phẩm hết hàng hoặc ngưng bán");
                }
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPrice(cartItem.getPrice());
                orderDetail.setTotalPrice(cartItem.getTotalPrice());

                orderDetails.add(orderDetail);
            }
        } else if (itemDTOs != null && !itemDTOs.isEmpty()) {
            for (ItemDTO dto : itemDTOs) {
                Product product = productRepository.findById(dto.getProductId())
                        .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

                if (!product.getStatus().equals(ProductStatus.AVAILABLE)) {
                    throw new RuntimeException("Sản phẩm hết hàng hoặc ngưng bán");
                }

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(dto.getQuantity());
                orderDetail.setPrice(dto.getPrice());
                orderDetail.setTotalPrice(dto.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));

                orderDetails.add(orderDetail);

            }
        } else {
            throw new RuntimeException("Không có sản phẩm nào để tạo đơn hàng");
        }
        return orderDetails;
    }

    @Override
    public void updateOrder(OrderDTO orderDTO, Long userId, Long orderId, String sessionId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        if (userId != null && userId > 0) {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            order.setUser(user);
        } else {
            order.setSessionId(sessionId);
        }

        if (orderDTO.getName() != null && !orderDTO.getName().isEmpty()) {
            order.setName(orderDTO.getName());
        }
        order.setStatus(orderDTO.getStatus());
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long userId, Long orderId, String sessionId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        if (userId != null && userId > 0) {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            order.setUser(user);
        } else {
            order.setSessionId(sessionId);
        }

        orderRepository.delete(order);
    }

    @Override
    public Page<OrderDTO> searchOrderByKey(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        Page<Order> orders = orderRepository.searchOrderByNameOrUserFullName(key, pageable);

        return orders.map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setName(order.getName());
            dto.setOrderDate(order.getOrderDate());
            dto.setStatus(order.getStatus());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setPaymentMethod(order.getPayment().getPaymentMethod());
            return dto;
        });
    }
}
