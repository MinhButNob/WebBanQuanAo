package com.example.banquanao.service;

import com.example.banquanao.model.Order;
import com.example.banquanao.model.OrderItem;
import com.example.banquanao.repository.OrderRepository;
import com.example.banquanao.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(Map<String, Object> orderData) {
        // Tạo order mới
        Order order = new Order();

        // Lấy userId (có thể null nếu chưa đăng nhập)
        if (orderData.get("userId") != null) {
            order.setUserId(Integer.valueOf(orderData.get("userId").toString()));
        }

        order.setCustomerName((String) orderData.get("customerName"));
        order.setPhone((String) orderData.get("phone"));
        order.setAddress((String) orderData.get("address"));

        if (orderData.get("note") != null) {
            order.setNote((String) orderData.get("note"));
        }

        order.setPaymentMethod((String) orderData.get("paymentMethod"));

        // Tính toán số tiền
        Double subtotal = Double.valueOf(orderData.get("subtotal").toString());
        Double vat = Double.valueOf(orderData.get("vat").toString());
        Double total = Double.valueOf(orderData.get("totalAmount").toString());

        order.setSubtotal(subtotal);
        order.setVat(vat);
        order.setTotal(total);

        // Lưu order
        Order savedOrder = orderRepository.save(order);

        // Lưu danh sách sản phẩm
        List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");

        for (Map<String, Object> itemData : items) {
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProductId(Long.valueOf(itemData.get("productId").toString()));
            item.setProductName((String) itemData.get("productName"));
            item.setSize((String) itemData.get("size"));
            item.setColor((String) itemData.get("color"));
            item.setQuantity(Integer.valueOf(itemData.get("quantity").toString()));
            item.setPrice(Double.valueOf(itemData.get("price").toString()));
            item.setImage((String) itemData.get("image"));

            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByUser(Integer userId) {
        return orderRepository.findByUserId(userId);
    }
}