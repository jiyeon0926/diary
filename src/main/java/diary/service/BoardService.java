package diary.service;

import diary.entity.Board;
import diary.entity.User;
import diary.repository.BoardRepository;
import diary.repository.ProfileRepository;
import diary.requestDto.CreateBoardRequestDto;
import diary.responseDto.BoardResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ProfileRepository profileRepository;
    private final BoardRepository boardRepository;

    public List<BoardResponseDto> findAll(){
        List<Board> boards = boardRepository.findAll();
        return boards.stream().map(BoardResponseDto::toDto).toList();
    }

    public BoardResponseDto findById(Long id) {
        return BoardResponseDto.toDto(findBoardById(id));

    }

    private Board findBoardById(Long id){
        return boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("올바르지 않은 ID입니다."));
    }

    @Transactional
    public BoardResponseDto saveBoard(CreateBoardRequestDto requestDto) {
        Board board = new Board(requestDto.getTitle(),requestDto.getContent(),requestDto.getWeather());
         Board savedBoard = boardRepository.save(board);
         return BoardResponseDto.toDto(savedBoard);
    }



}
