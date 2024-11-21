package diary.responseDto;

import diary.entity.Board;

import java.time.LocalDateTime;

public class BoardResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String weather;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    public BoardResponseDto(Long id, String title, String content, String weather, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id= id;
        this.title= title;
        this.content = content;
        this.weather = weather;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

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
