package diary.controller.dto;

import diary.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentFindAllResponseDto {

    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private Long likeCount = 0L;

    public static CommentFindAllResponseDto toDto(Comment comment) {
        return new CommentFindAllResponseDto(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
    }
}