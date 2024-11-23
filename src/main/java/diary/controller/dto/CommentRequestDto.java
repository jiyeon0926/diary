package diary.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotNull
    private final String content;

    @JsonCreator
    public CommentRequestDto(String content) {
        this.content = content;
    }
}
