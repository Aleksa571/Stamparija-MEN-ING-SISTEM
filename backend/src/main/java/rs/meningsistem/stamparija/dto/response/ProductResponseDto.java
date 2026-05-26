package rs.meningsistem.stamparija.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String dimensions;
    private String imageUrl;
    private Integer stock;
    private Boolean available;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String categoryName;
}
