package com.example.banquanao.controller;

import com.example.banquanao.model.Order;
import com.example.banquanao.repository.OrderRepository;
import com.example.banquanao.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Chỉ định cụ thể thay vì "*"
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    // ============== USER APIs ==============

    // Tạo đơn hàng mới
    @PostMapping("/orders/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderData) {
        try {
            Order newOrder = orderService.createOrder(orderData);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đặt hàng thành công!");
            response.put("id", newOrder.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi tạo đơn hàng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Lấy đơn hàng theo ID (cho user)
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            if (order != null) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Không tìm thấy đơn hàng"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }

    // Lấy danh sách đơn hàng của user
    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<?> getOrdersByUser(@PathVariable Integer userId) {
        try {
            List<Order> orders = orderService.getOrdersByUser(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi server: " + e.getMessage()));
        }
    }

    // ============== ADMIN APIs ==============

    // Lấy tất cả đơn hàng (Admin)
    @GetMapping("/admin/orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi: " + e.getMessage()));
        }
    }

    // Lấy chi tiết đơn hàng (Admin)
    @GetMapping("/admin/orders/{id}")
    public ResponseEntity<?> getOrderByIdAdmin(@PathVariable Long id) {
        try {
            Order order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                return ResponseEntity.ok(order);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Không tìm thấy đơn hàng"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi: " + e.getMessage()));
        }
    }

    // Cập nhật trạng thái đơn hàng (Admin)
    @PutMapping("/admin/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            Order order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                String newStatus = body.get("status");
                if (newStatus != null) {
                    order.setStatus(newStatus);
                    orderRepository.save(order);
                    return ResponseEntity.ok(Map.of("message", "Cập nhật thành công"));
                }
                return ResponseEntity.badRequest().body(Map.of("message", "Thiếu trạng thái"));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi: " + e.getMessage()));
        }
    }

    // Xóa đơn hàng (Admin) - ĐÃ BỎ COMMENT
    @DeleteMapping("/admin/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            if (orderRepository.existsById(id)) {
                orderRepository.deleteById(id);
                return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi xóa: " + e.getMessage()));
        }
    }

}