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
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<List<BoardResponseDto>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam (defaultValue = "10")int size) {

        Pageable pageables = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        return ResponseEntity.ok().body(boardService.findAll(pageables));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(boardService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BoardResponseDto> save(@RequestBody CreateBoardRequestDto requestDto,HttpServletRequest reqeust) {
        HttpSession session = reqeust.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.saveBoard(requestDto,loginUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> update(@PathVariable Long id, @RequestBody CreateBoardRequestDto requestDto,HttpServletRequest request) {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        return ResponseEntity.ok().body(boardService.update(id,requestDto,loginUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id,HttpServletRequest request) {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        boardService.delete(id,loginUser);
        return ResponseEntity.ok().body("삭제완료");
    }

}
