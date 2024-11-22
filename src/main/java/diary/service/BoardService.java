package diary.service;

import diary.entity.Board;
import diary.entity.User;
import diary.repository.BoardRepository;
import diary.repository.ProfileRepository;
import diary.requestDto.CreateBoardRequestDto;
import diary.responseDto.BoardResponseDto;
import diary.responseDto.FollowResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ProfileRepository profileRepository;
    private final BoardRepository boardRepository;

    public List<BoardResponseDto> findAll(Pageable pageable) {
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        //JPA에서 제공하는 페이징처리 메소드를 작성한다.
        Page<Board> boardPage = boardRepository.findAll(pageable);
        //boardPage객체에서 데이터베이스에서 가져온 데이터를 가져온다
        List<Board> boardList = boardPage.getContent();

        //boardList에서 보드객체를 하나씩 꺼내와서 BoardResponseDto객체로 변환한 후 boardResponseDtoList에 저장한다.
        for (Board board : boardList) {
            BoardResponseDto boardResponseDto = BoardResponseDto.toDto(board);
            boardResponseDtoList.add(boardResponseDto);
        }

        return boardResponseDtoList;
    }

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


    public List<BoardResponseDto> findAllByFollowingUsers(Long userId, Pageable pageable) {
                List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

                // 게시글 가져오기
                Page<BoardResponseDto> boardList = boardRepository.findAllByFollowingUsers(userId, pageable);

                // 게시글을 DTO로 변환
                for (BoardResponseDto board : boardList) {
                    boardResponseDtoList.add(board);
        }
        return boardResponseDtoList;
    }


    public List<BoardResponseDto> findPostsByPeriod(LocalDate startDate, LocalDate endDate, Pageable pageable) {

        // LocalDate를 LocalDateTime으로 변환
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Page<BoardResponseDto> boards = boardRepository.findByCreatedAtBetween(startDateTime, endDateTime, pageable);

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (BoardResponseDto board : boards) {
            boardResponseDtoList.add(board);
        }
        return boardResponseDtoList;
    }

    public List<BoardResponseDto> findPostsByFollowingUsersAndPeriod(Long id, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Page<BoardResponseDto> boards = boardRepository.findByFollowingUsersAndPeriod(id, startDateTime, endDateTime, pageable);

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (BoardResponseDto board : boards) {
            boardResponseDtoList.add(board);
        }
        return boardResponseDtoList;
    }

    //전체 기간의 좋아요 순 정렬
//    public List<BoardResponseDto> findAllByGood(Pageable pageable) {
//        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
//
//        // 게시글 가져오기
//        Page<BoardResponseDto> boardList = boardRepository.findAllOrderByGoodConut(pageable);
//        for (Board board : boardList) {
//            boardResponseDtoList.add(BoardResponseDto.toDto(board));
//        }
//        return boardList;
//    }

    public List<BoardResponseDto> findAllByGood(Pageable pageable) {
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        // 게시글 가져오기
        Page<BoardResponseDto> boardList = boardRepository.findAllOrderByGoodConut(pageable);
        for (BoardResponseDto board : boardList) {
            boardResponseDtoList.add(board);
        }
        return boardResponseDtoList;
    }

    //특정기간의 좋아요 순 정렬
    public List<BoardResponseDto> findPostsByPeriodAndGood(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        // 게시글 가져오기
        Page<BoardResponseDto> boardList = boardRepository.findPostsByPeriodAndGood(startDateTime,endDateTime,pageable);

        for (BoardResponseDto board : boardList) {
            boardResponseDtoList.add(board);
        }

        return boardResponseDtoList;
    }

    //팔로우한 유저들의 기간별 게시물 좋아요 많은 순 조회
    public List<BoardResponseDto> findPostsByFollowingUsersAndPeriodAndGood(Long id, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        Page<BoardResponseDto> boardList = boardRepository.findByFollowingUsersAndPeriodAndGood(id,startDateTime,endDateTime,pageable);
        for(BoardResponseDto board : boardList) {
            boardResponseDtoList.add(board);
        }
        return boardResponseDtoList;
    }

    //팔로우한 유저들의 전체기간 게시물 좋아요 순 조회
    public List<BoardResponseDto> findAllByFollowingUsersAndGood(Long id, Pageable pageable) {
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        // 게시글 가져오기
        Page<BoardResponseDto> boardList = boardRepository.findAllByFollowingUsersGood(id,pageable);
        for (BoardResponseDto board : boardList) {
            boardResponseDtoList.add(board);
        }
        return boardResponseDtoList;
    }
}
