package diary.controller;

import diary.requestDto.GoodRequestDto;
import diary.responseDto.BoardResponseDto;
import diary.responseDto.GoodResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodController {
    @PostMapping("/board/{id}")
    public ResponseEntity<GoodResponseDto> good(@PathVariable Long id, @RequestBody GoodRequestDto goodRequestDto){

        return ResponseEntity<>(goodRequestDto);
    }
}
