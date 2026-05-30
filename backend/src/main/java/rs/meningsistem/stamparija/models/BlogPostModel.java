package rs.meningsistem.stamparija.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostModel {
    private Long id;

    @NotBlank(message = "Naslov je obavezan")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Sadrzaj je obavezan")
    @Size(max = 10000)
    private String content;

    @Size(max = 500)
    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long authorId;
    private String authorUsername;
    private String authorFullName;
}
