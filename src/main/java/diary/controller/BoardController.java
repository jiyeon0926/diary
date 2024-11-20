package diary.controller;

import diary.requestDto.CreateBoardRequestDto;
import diary.responseDto.BoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    @PostMapping
    public ResponseEntity<BoardResponseDto> save(@RequestBody CreateBoardRequestDto requestDto){

    }
}
