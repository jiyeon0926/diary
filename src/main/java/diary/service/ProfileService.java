package diary.service;

import diary.entity.User;
import diary.repository.ProfileRepository;
import diary.requestDto.ProfileRequestDto;
import diary.responseDto.ProfileResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileResponseDto findProfileById(Long id){
        User user = profileRepository.findUserByUserIdOrElseThrow(id);
        return new ProfileResponseDto(user);
    }

    public void updatePasswordById(Long id){
        User user = profileRepository.findUserByUserIdOrElseThrow(id);
    }

    public void checkPasswordById(Long id){
        profileRepository.findById(id);
    }
}
