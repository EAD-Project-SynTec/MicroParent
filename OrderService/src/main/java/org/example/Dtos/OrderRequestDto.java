package org.example.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Models.OrderItem;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    @Id
    private String id;
    private String userId;
    private Date dateCreated;
    private List<OrderItem> items;
}
