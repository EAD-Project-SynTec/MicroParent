package org.example.Dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithDetailsDto {
    private String orderId;
    private String userId;
    private String address;
    private Date dateCreated;
    private String status;
    private List<OrderItemDetailsDto> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemDetailsDto {
        private int productId;
        private int quantity;
        private double price;
        private String productName;
        private String productImageUrl;
    }
}
