package diary.service;

import diary.entity.ClassId;
import diary.entity.Follow;
import diary.entity.User;
import diary.repository.FollowRepository;
import diary.repository.ProfileRepository;
import diary.responseDto.FollowResponseDto;
import diary.responseDto.FollowerResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;

    // 팔로잉
    public void follow(HttpSession session, Long followeeId) {
        // 팔로우 하는 사람
        User follower = (User) session.getAttribute("loginUser");

        // 팔로우 대상이 존재하는 확인
        User followee = profileRepository.findUserByidOrElseThrow(followeeId);

        // 팔로우 관계인지 확인
        Follow follow = followRepository.findByFollowerAndFollowee(follower.getId(), followee.getId());

        // 자기 자신을 팔로우 할 수 없음
        if (follower.getId() == followee.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't follow yourself.");
        }

        if (follow == null) {
            ClassId id = new ClassId(follower.getId(), followee.getId());
            Follow following = new Follow(id, follower, followee);

            followRepository.save(following);
        }
    }

    // 팔로잉 취소
    public void unfollow(HttpSession session, Long followeeId) {
        User follower = (User) session.getAttribute("loginUser");

        User followee = profileRepository.findUserByidOrElseThrow(followeeId);

        // 팔로우 관계인지 확인
        Follow follow = followRepository.findByFollowerAndFollowee(follower.getId(), followee.getId());

        if (follow == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow relationship not found");
        }

        followRepository.delete(follow);
    }

    // 팔로우 전체 조회
    public List<FollowResponseDto> findAllByFollowList(Long userId) {
        List<Follow> follows = followRepository.findByFollowerIdOrderByCreatedAtDesc(userId);
        return follows.stream()
                .map(follow -> new FollowResponseDto(follow))
                .collect(Collectors.toList());
    }

    // 팔로워 전체 조회
    public List<FollowerResponseDto> findAllByFollowerList(Long userId) {
        List<Follow> followers = followRepository.findByFolloweeIdOrderByCreatedAtDesc(userId);
        return followers.stream()
                .map(follower -> new FollowerResponseDto(follower))
                .collect(Collectors.toList());
    }
}
