package diary.controller.dto;

import diary.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {

    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
    }
}