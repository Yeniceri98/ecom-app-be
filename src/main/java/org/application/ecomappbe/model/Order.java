package org.application.ecomappbe.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")     // "Order" is a reserved keyword in SQL
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String orderEmail;
    private LocalDate orderDate;

    private Double totalAmount;
    private String orderStatus;

    @OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToOne
    @JoinColumn(name = "payment_id")
    @ToString.Exclude
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

}
