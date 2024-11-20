package diary.requestDto;

import lombok.Getter;

@Getter
public class CreateBoardRequestDto {
    private final Long userId;
    private final String title;
    private final String content;
    private final String weather;

    public CreateBoardRequestDto(Long userId, String title, String content, String weather) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.weather = weather;
    }

}
