package diary.controller;

import diary.entity.User;
import diary.requestDto.CreateBoardRequestDto;
import diary.responseDto.BoardResponseDto;
import diary.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //boards?page=0&size=10
    @GetMapping()
    public ResponseEntity<List<BoardResponseDto>> findAll(Pageable pageable) {
        Pageable pageables = PageRequest.of(0,1);
        return ResponseEntity.ok().body(boardService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(boardService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BoardResponseDto> save(@RequestBody CreateBoardRequestDto requestDto,HttpServletRequest  reqeust) {
        HttpSession session = reqeust.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.saveBoard(requestDto,loginUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> update(@PathVariable Long id, @RequestBody CreateBoardRequestDto requestDto) {
        return ResponseEntity.ok().body(boardService.update(id,requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.ok().body("삭제완료");
    }

}
