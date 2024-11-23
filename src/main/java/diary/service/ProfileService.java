package diary.service;

import diary.config.PasswordEncoder;
import diary.entity.User;
import diary.repository.ProfileRepository;
import diary.controller.dto.ProfileRequestDto;
import diary.controller.dto.ProfileResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 프로필 단건 조회
    public ProfileResponseDto findProfileById(Long id){
        User user = profileRepository.findUserByidOrElseThrow(id);
        return new ProfileResponseDto(user);
    }
    
    // 비밀번호 확인 후 프로필 변경
    public void updatePasswordById(Long id, ProfileRequestDto profileRequestDto){
        User user = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("User profile not found"));

        if(checkPassword(id, profileRequestDto)){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "비밀번호가 틀렸습니다.");
        }
        // 프로필 변경
        updateProfile(id, profileRequestDto);
    }

    // ID로 프로필 변경
    public void updateById(Long id, ProfileRequestDto profileRequestDto){
        User userProfile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("User profile not found"));
        userProfile.setUsername(profileRequestDto.getUsername());
        userProfile.setPassword(passwordEncoder.encode(profileRequestDto.getPassword()));
        profileRepository.save(userProfile);
    }
    // 프로필 변경
    private void updateProfile(Long id, ProfileRequestDto profileRequestDto){
        User userProfile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("User profile not found"));
        userProfile.setUsername(profileRequestDto.getUsername());
        profileRepository.save(userProfile);
    }

    //인코딩된 암호로 사용자 확인
    private boolean checkPassword(Long id, ProfileRequestDto profileRequestDto) {
        // oldPassword 는 DB에 저장된 값, newPassword는 RequestBody 에서 수신한 값
        User user = profileRepository.findUserByidOrElseThrow(id);
        String oldPassword = user.getPassword();

        // URL RequestBody에서 가져온 password
        String newPassword = profileRequestDto.getPassword();

        if(passwordEncoder.matches(newPassword, oldPassword)){
            return false;
        }
        return true;
    }
}
