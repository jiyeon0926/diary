package diary.service;

import diary.entity.Board;
import diary.entity.User;
import diary.repository.BoardRepository;
import diary.controller.dto.CreateBoardRequestDto;
import diary.controller.dto.BoardResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    //정렬값 검증
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("createdAt", "modifiedAt", "good");

    //게시물 단건 조회
    public BoardResponseDto findById(Long id) {
        return boardRepository.findByIdWhitGoodCountOrElseThrow(id);
    }

    //게시물 저장
    @Transactional
    public BoardResponseDto saveBoard(CreateBoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto.getTitle(), requestDto.getContent(), requestDto.getWeather(), user);
        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.toDto(savedBoard);
    }

    //게시물 업데이트
    @Transactional
    public BoardResponseDto update(Long id, CreateBoardRequestDto requestDto, User user) {
        // Optional 처리
        Board board = boardRepository.findByIdOrElseThrow(id);

        // 작성자 확인
        if (!board.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        // 게시글 수정
        board.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getWeather(), user);

        return boardRepository.findByIdWhitGoodCountOrElseThrow(id);
    }

    //게시물 삭제
    public void delete(Long id, User user) {
        Board board = boardRepository.findByIdOrElseThrow(id);
        if (!board.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }
        boardRepository.deleteById(id);
    }

    public List<BoardResponseDto> findBoardAll(Pageable pageable, String sortBy, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(sortBy + "는(은) 사용할 수 없는 정렬값입니다.");
        }

        Page<BoardResponseDto> boardResponseDtoPage;

        // 좋아요 순 정렬
        if ("good".equals(sortBy)) {

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "gb.board"));

            if (startDateTime != null && endDateTime != null) {
                //기간별 좋아요 많은 순 조회
                boardResponseDtoPage = boardRepository.findPostsByPeriodAndGood(startDateTime, endDateTime, pageable);
            } else {
                //전체기간 좋아요 많은 순 조회
                boardResponseDtoPage = boardRepository.findAllOrderByGoodConut(pageable);
            }
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, sortBy));

            if (startDateTime != null && endDateTime != null) {
                //기간별 조회

                boardResponseDtoPage = boardRepository.findByCreatedAtBetween(startDateTime, endDateTime, pageable);
            } else {
                //전체기간 조회
                return mapToBoardResponseDtos(boardRepository.findAll(pageable).getContent());
            }
        }

        return mapToBoardResponseDtos(boardResponseDtoPage.getContent());
    }



    public List<BoardResponseDto> findAllFollow(Long id, Pageable pageable, String sortBy, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        System.out.println("팔로우 게시글 기능확인");
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(sortBy + "는(은) 사용할 수 없는 정렬값입니다.");
        }
        Page<BoardResponseDto> boardResponseDtoPage;

        if("good".equals(sortBy)) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "gb.board"));
            if(startDate != null && endDate != null) {

                //내가 팔로우한 사람의 게시글을 기간별 좋아요 많은 순 조회
                System.out.println("내가 팔로우한 사람의 게시글을 기간별 좋아요 많은 순 조회");
                boardResponseDtoPage = boardRepository.findByFollowingUsersAndPeriodAndGood(id, startDateTime,endDateTime,pageable);
            }else{
                //내가 팔로우한  사람의 게시글을 전체기간 좋아요 많은 순 조회  현재오류
                System.out.println("내가 팔로우한  사람의 게시글을 전체기간 좋아요 많은 순 조회  현재오류");
                boardResponseDtoPage = boardRepository.findAllByFollowingUsersGood(id, pageable);
            }
        }else{
            if(startDate != null && endDate != null) {
                //팔로우한 사람들의 게시글 기간별 조회
                System.out.println("팔로우한 사람들의 게시글 기간별 조회");
                boardResponseDtoPage = boardRepository.findByFollowingUsersAndPeriod(id, startDateTime, endDateTime, pageable);
            }else{
                //팔로우한 사람들의 게시글 전체기간 조회
                System.out.println("팔로우한 사람들의 게시글 전체기간 조회");
                boardResponseDtoPage = boardRepository.findAllByFollowingUsers(id, pageable);
            }
        }
        return mapToBoardResponseDtos(boardResponseDtoPage.getContent());
    }

    //Dto List형식으로 반환
    private List<BoardResponseDto> mapToBoardResponseDtos(List<?> boards) {
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Object board : boards) {
            if (board instanceof Board) {
                boardResponseDtoList.add(BoardResponseDto.toDto((Board) board));
            } else if (board instanceof BoardResponseDto) {
                boardResponseDtoList.add((BoardResponseDto) board);
            }
        }
        return boardResponseDtoList;
    }
}
