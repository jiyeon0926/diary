package diary.service;

import diary.entity.Board;
import diary.entity.GoodBoard;
import diary.entity.User;
import diary.entity.UserBoardId;
import diary.repository.BoardRepository;
import diary.repository.GoodBoardRepository;
import diary.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class GoodBoardService {
    GoodBoardRepository goodBoardRepository;
    BoardRepository boardRepository;
    UserRepository userRepository;

    // 게시글에 좋아요
    public void doGood(Long userId, Long boardId) {

        // 유저 찾기, 게시글 찾기
        User findUser = userRepository.findByIdElseThrow(userId);
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Long boardUserId = findBoard.getUser().getId();

        // 만약 본인이 쓴글이면 400 상태코드 throw
        if (boardUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 좋아요게시글 테이블에서 게시글 id와 유저 id로 찾기
        GoodBoard findGoodBoard = goodBoardRepository.findByBoardAndUser(boardId, userId);

        // 만약 좋아요를 안하면 좋아요하기, 좋아요 했으면 400 상태코드
        if (findGoodBoard == null) {
            UserBoardId userBoardId = new UserBoardId(userId, boardId);
            GoodBoard goodBoard = new GoodBoard(userBoardId, findUser, findBoard);

            goodBoardRepository.save(goodBoard);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요한 글입니다");
        }
    }

    // 게시글에 좋아요 취소
    public void undoGood(Long userId, Long boardId) {

        // 게시글 찾기
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Long boardUserId = findBoard.getUser().getId();

        // 만약 본인이 쓴글이면 400 상태코드 throw
        if (boardUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 좋아요게시글 테이블에서 게시글 id와 유저 id로 찾기
        GoodBoard findGoodBoard = goodBoardRepository.findByBoardAndUser(boardId, userId);

        // 만약 좋아요를 했으면 좋아요취소하기, 좋아요 안했으면 400 상태코드
        if (findGoodBoard == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요가 이미 취소되어 있습니다");

        } else {
            goodBoardRepository.delete(findGoodBoard);
        }
    }
}
