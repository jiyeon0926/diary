package diary.service;

import diary.entity.FollowId;
import diary.entity.Follow;
import diary.entity.User;
import diary.repository.FollowRepository;
import diary.repository.ProfileRepository;
import diary.controller.dto.FollowResponseDto;
import diary.controller.dto.FollowerResponseDto;
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
    public void follow(User follower, Long followeeId) {
        User followee = profileRepository.findUserByidOrElseThrow(followeeId);
        Follow follow = followRepository.findByFollowerAndFollowee(follower.getId(), followee.getId());

        if (follower.getId() == followee.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자신을 팔로우 할 수 없습니다.");
        }

        if (follow == null) {
            FollowId id = new FollowId(follower.getId(), followee.getId());
            Follow following = new Follow(id, follower, followee);

            followRepository.save(following);
        }
    }

    // 팔로잉 취소
    public void unfollow(User follower, Long followeeId) {
        User followee = profileRepository.findUserByidOrElseThrow(followeeId);
        Follow follow = followRepository.findByFollowerAndFollowee(follower.getId(), followee.getId());

        if (follow == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "팔로우 관계가 아닙니다.");
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
