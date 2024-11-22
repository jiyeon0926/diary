package diary.controller;

import diary.entity.User;
import diary.requestDto.CreateBoardRequestDto;
import diary.responseDto.BoardResponseDto;
import diary.service.BoardService;
import diary.service.FollowService;
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
    private final FollowService followService;

    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("createdAt", "modifiedAt","good");

    //boards?page=0&size=10
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> findAll(@Valid
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "createdAt") String sortBy,
                                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(sortBy + "는(은) 사용할 수 없는 정렬값입니다: ");
        }
        // Pageable 객체 생성
        Pageable pageable;
        List<BoardResponseDto> boards;

        // 좋아요 순 정렬
        if (sortBy.equals("good")) {
            pageable = PageRequest.of(page, size);
            if (startDate != null && endDate != null) {
                boards = boardService.findPostsByPeriodAndGood(startDate, endDate, pageable); // 좋아요 + 기간 필터링
            } else {
                boards = boardService.findAllByGood(pageable); // 좋아요 순 정렬
            }
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
            if (startDate != null && endDate != null) {
                boards = boardService.findPostsByPeriod(startDate, endDate, pageable); // 기간 필터링
            } else {
                boards = boardService.findAll(pageable); // 기본 정렬
            }
        }

        return ResponseEntity.ok().body(boards);
    }

    @GetMapping("/following")
    public ResponseEntity<List<BoardResponseDto>> findFollowboard(@Valid
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                                  HttpServletRequest reqeust) {

        HttpSession session = reqeust.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인되지 않은 경우
        }

        // 허용된 정렬 기준만 처리
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }Pageable pageable;
        List<BoardResponseDto> boards;

        if(sortBy.equals("good")) {
            pageable = PageRequest.of(page, size);
            if(startDate != null && endDate != null) {
                //내가 팔로우한 사람의 게시글을 기간별 좋아요 많은 순 조회
                boards = boardService.findPostsByFollowingUsersAndPeriodAndGood(loginUser.getId(), startDate,endDate,pageable);
            }else{
                //내가 팔로우한  사람의 게시글을 전체기간 좋아요 많은 순 조회
                boards = boardService.findAllByFollowingUsersAndGood(loginUser.getId(),pageable);
            }
        }else{
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
            if(startDate != null && endDate != null) {
                //팔로우한 사람들의 게시글 기간별 조회
                boards = boardService.findPostsByFollowingUsersAndPeriod(loginUser.getId(), startDate, endDate, pageable);
            }else{
                boards = boardService.findAllByFollowingUsers(loginUser.getId(), pageable);
            }
        }


        return ResponseEntity.ok(boards);
    }

    //단건조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(boardService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BoardResponseDto> save(@Valid @RequestBody CreateBoardRequestDto requestDto, HttpServletRequest reqeust) {
        HttpSession session = reqeust.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.saveBoard(requestDto, loginUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> update(@PathVariable Long id, @RequestBody CreateBoardRequestDto requestDto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        return ResponseEntity.ok().body(boardService.update(id, requestDto, loginUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        boardService.delete(id, loginUser);
        return ResponseEntity.ok().body("삭제완료");
    }

}
