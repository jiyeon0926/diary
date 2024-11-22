package diary.responseDto;

import diary.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String weather;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private Long likeCount;


    public static BoardResponseDto toDto(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getWeather(),
                board.getCreatedAt(),
                board.getModifiedAt()
        );
    }
}
