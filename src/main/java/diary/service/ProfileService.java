package diary.service;

import diary.config.PasswordEncoder;
import diary.entity.User;
import diary.repository.ProfileRepository;
import diary.requestDto.ProfileRequestDto;
import diary.responseDto.ProfileResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ProfileResponseDto findProfileById(Long id){
        User user = profileRepository.findUserByidOrElseThrow(id);
        return new ProfileResponseDto(user);
    }

    public void updatePasswordById(Long id){
        User user = profileRepository.findUserByidOrElseThrow(id);
    }

    public boolean checkPassword(Long id, ProfileRequestDto profileRequestDto) {
        User user = profileRepository.findUserByidOrElseThrow(id);
        String oldPassword = user.getPassword();
        String NewPassword = profileRequestDto.getPassword(); // URL Request에서 가져온 password

        if(!oldPassword.equals(NewPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return true;
    }
}
