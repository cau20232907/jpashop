package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Delivery extends BaseEntity {

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}