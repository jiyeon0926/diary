package diary.service;

import diary.config.PasswordEncoder;
import diary.controller.dto.PasswordRequestDto;
import diary.entity.User;
import diary.repository.ProfileRepository;
import diary.controller.dto.ProfileRequestDto;
import diary.controller.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    // 프로필 단건 조회
    public ProfileResponseDto findProfileById(Long id){
        User user = profileRepository.findUserByidOrElseThrow(id);
        return new ProfileResponseDto(user);
    }
    
    // 비밀번호 확인 후
    public void comparePassword(Long id, PasswordRequestDto passwordRequestDto){
        if(checkPassword(id, passwordRequestDto)){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "비밀번호가 틀렸습니다.");
        }
    }

    // ID로 프로필 변경
    public void updateById(Long id, String name, String password){
        User userProfile = profileRepository.findUserByidOrElseThrow(id);
        String oldPassword = userProfile.getPassword();
        String newPassword = password;

        boolean matches = passwordEncoder.matches(newPassword, oldPassword);

        if (matches) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "동일한 비밀번호는 사용할 수 없습니다.");
        }

        userProfile.setUsername(name);
        userProfile.setPassword(passwordEncoder.encode(newPassword));
        profileRepository.save(userProfile);
    }

    //인코딩된 암호로 사용자 확인
    private boolean checkPassword(Long id, PasswordRequestDto passwordRequestDto) {
        // oldPassword 는 DB에 저장된 값, newPassword는 RequestBody 에서 수신한 값
        User user = profileRepository.findUserByidOrElseThrow(id);
        String oldPassword = user.getPassword();

        // URL RequestBody에서 가져온 password
        String newPassword = passwordRequestDto.getPassword();

        if(passwordEncoder.matches(newPassword, oldPassword)){
            return false;
        }
        return true;
    }
}
