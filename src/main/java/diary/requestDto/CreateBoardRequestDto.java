package diary.requestDto;

import lombok.Getter;

@Getter
public class CreateBoardRequestDto {
    private Long
    private final String title;
    private final String content;
    private final String weather;

    public CreateBoardRequestDto(String title, String content, String weather) {
        this.title = title;
        this.content = content;
        this.weather = weather;
    }
}
