package diary.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentUpdateResponseDto {

    private final Long id;
    private final String content;
    private final LocalDateTime modifiedAt;
    private Long likeCount = 0L;
}
