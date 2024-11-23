package diary.controller;

import diary.controller.dto.PasswordRequestDto;
import diary.controller.dto.ProfileRequestDto;
import diary.controller.dto.ProfileResponseDto;
import diary.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users/{id}/profile")
public class ProfileController {
    private final ProfileService profileService;

    // 프로필 단건 조회
    @GetMapping
    public ResponseEntity<ProfileResponseDto> findById(@PathVariable Long id){
        ProfileResponseDto profileById = profileService.findProfileById(id);
        return new ResponseEntity<>(profileById, HttpStatus.OK);
    }

    // 프로필 수정하기 전, 비밀번호 확인
    @PostMapping
    public ResponseEntity<Void> comparePassword(@PathVariable Long id, @RequestBody PasswordRequestDto passwordRequestDto, HttpSession session){
        profileService.comparePassword(id, passwordRequestDto);
        session.setAttribute("allowUpdate", true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 프로필 수정
    @PatchMapping
    public ResponseEntity<Void> updateById(@PathVariable Long id, @RequestBody ProfileRequestDto profileRequestDto, HttpSession session){
        Boolean allowUpdate = (Boolean) session.getAttribute("allowUpdate");

        if (allowUpdate == null || !allowUpdate) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        profileService.updateById(id, profileRequestDto);
        session.setAttribute("allowUpdate", false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
