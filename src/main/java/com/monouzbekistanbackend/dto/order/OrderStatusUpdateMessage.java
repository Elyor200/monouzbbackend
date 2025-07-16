package com.monouzbekistanbackend.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateMessage {
    private UUID orderId;
    private String status;
}
