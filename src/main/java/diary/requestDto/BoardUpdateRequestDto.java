package diary.requestDto;

import lombok.Getter;
@Getter
public class BoardUpdateRequestDto {


        private final Long userId;
        private final String title;
        private final String content;
        private final String weather;

        public BoardUpdateRequestDto(Long userId, String title, String content, String weather) {
            this.userId = userId;
            this.title = title;
            this.content = content;
            this.weather = weather;
        }
}
