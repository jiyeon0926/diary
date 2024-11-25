package diary.controller;

import diary.entity.User;
import diary.controller.dto.CreateBoardRequestDto;
import diary.controller.dto.BoardResponseDto;
import diary.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //정렬값 검증
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("createdAt", "modifiedAt","good");

    /*
     * 전체 게시글 조회
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 (기본값: createdAt)
     * @param startDate 필터링 시작 날짜 (옵션)
     * @param endDate 필터링 종료 날짜 (옵션)
     * @return 정렬 및 필터링된 게시글 목록
    */
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> findAll(@Valid
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "createdAt") String sortBy,
                                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        List<BoardResponseDto> boards = boardService.findBoardAll(pageable, sortBy, startDate, endDate);

        return ResponseEntity.ok().body(boards);
    }

    /*
     * 팔로우한 사용자의 게시글 조회
     * @param page = 페이지 번호 (기본값: 0)
     * @param size = 페이지 크기 (기본값: 10)
     * @param sortBy = 정렬 기준 (기본값: createdAt)
     * @param startDate = 필터링 시작 날짜 (옵션)
     * @param endDate = 필터링 종료 날짜 (옵션)
     * @return 정렬 및 필터링된 팔로우 사용자의 게시글 목록
     */
    @GetMapping("/following")
    public ResponseEntity<List<BoardResponseDto>> findFollowboard(@Valid
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                                  HttpServletRequest request) {

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");



        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        List<BoardResponseDto> boards = boardService.findAllFollow(loginUser.getId(),pageable, sortBy, startDate, endDate);

        return ResponseEntity.ok().body(boards);
    }

    /*
     * 게시글 단건 조회
     * @param id = 게시글 ID
     * @return 게시글 데이터
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(boardService.findById(id));
    }

    //게시글 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> save(@Valid @RequestBody CreateBoardRequestDto requestDto, HttpServletRequest reqeust) {
        HttpSession session = reqeust.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.saveBoard(requestDto, loginUser));
    }

    /* 게시글 수정
     * @param id = 게시글 ID
     * @return 수정된 게시글 데이터
     */
    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> update( @PathVariable Long id,@Valid @RequestBody CreateBoardRequestDto requestDto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        return ResponseEntity.ok().body(boardService.update(id, requestDto, loginUser));
    }

    /* 게시글 삭제
     * @param id = 게시글 ID
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        boardService.delete(id, loginUser);
        return ResponseEntity.ok().body("삭제완료");
    }

}
