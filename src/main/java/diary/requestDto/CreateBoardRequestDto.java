package diary.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateBoardRequestDto {

    @NotBlank(message = "제목은 필수 값 입니다.")
    private final String title;
    @NotBlank(message = "내용은 필수 값 입니다.")
    private final String content;
    @NotBlank(message = "날씨는 필수 값 입니다.")
    @Size(max = 2, message="날씨는 두글자 이내여야 합니다.")
    private final String weather;

}
