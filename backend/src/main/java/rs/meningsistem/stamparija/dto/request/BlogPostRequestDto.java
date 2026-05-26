package rs.meningsistem.stamparija.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogPostRequestDto {

    @NotBlank(message = "Naslov je obavezan")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Sadrzaj je obavezan")
    @Size(max = 10000)
    private String content;

    @Size(max = 500)
    private String imageUrl;
}
