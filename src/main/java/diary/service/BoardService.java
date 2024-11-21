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
import java.util.stream.Collectors;

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

    public BoardResponseDto findById(Long id) {
        return BoardResponseDto.toDto(findBoardById(id));
    }

    private Board findBoardById(Long id){
        return boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("올바르지 않은 ID값입니다."));
    }


    @Transactional
    public BoardResponseDto saveBoard(CreateBoardRequestDto requestDto,User user) {
        Board board = new Board(requestDto.getTitle(),requestDto.getContent(),requestDto.getWeather(),user);
         Board savedBoard = boardRepository.save(board);
         return BoardResponseDto.toDto(savedBoard);
    }

    @Transactional
    public BoardResponseDto update(Long id, CreateBoardRequestDto requestDto,User user) {
        Board board = findBoardById(id);

        if (!board.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }

        board.update(requestDto.getTitle(),requestDto.getContent(),requestDto.getWeather(),user);
        return BoardResponseDto.toDto(board);
    }

    public void delete(Long id,User user) {
        Board board = findBoardById(id);
        if (!board.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }

        boardRepository.deleteById(id);
    }

    public List<BoardResponseDto> findAllByFollowingUsers(List<FollowResponseDto> followingUserIds, Pageable pageable) {
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        // 게시글 가져오기
        Page<Board> boardPage = boardRepository.findAllByUserIdIn(followingUserIds, pageable);

        // 게시글을 DTO로 변환
        for (Board board : boardPage.getContent()) {
            boardResponseDtoList.add(BoardResponseDto.toDto(board));
        }

        return boardResponseDtoList;
    }

    public List<BoardResponseDto> findAllByFollowingUsersTest(Long userId, Pageable pageable) {
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        // 게시글 가져오기
        Page<Board> boardList = boardRepository.findAllByFollowingUsers(userId,pageable);

        // 게시글을 DTO로 변환
       for (Board board : boardList) {
            boardResponseDtoList.add(BoardResponseDto.toDto(board));
        }
        return boardResponseDtoList;
    }

    public List<BoardResponseDto> findPostsByPeriod(LocalDate startDate, LocalDate endDate,Pageable pageable) {

        // LocalDate를 LocalDateTime으로 변환
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Board> boards = boardRepository.findByCreatedAtBetween(startDateTime, endDateTime, pageable);

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boards) {
            boardResponseDtoList.add(BoardResponseDto.toDto(board));
        }
        return boardResponseDtoList;
    }

    public List<BoardResponseDto> findPostsByFollowingUsersAndPeriod(Long id, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Board> boards = boardRepository.findByFollowingUsersAndPeriod(id, startDateTime, endDateTime, pageable);

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boards) {
            boardResponseDtoList.add(BoardResponseDto.toDto(board));
        }
        return boardResponseDtoList;
    }

}
