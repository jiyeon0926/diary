package diary.controller;

import diary.entity.User;
import diary.requestDto.ProfileRequestDto;
import diary.responseDto.ProfileResponseDto;
import diary.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users/{id}/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> findById(@PathVariable Long id){
        ProfileResponseDto profileById = profileService.findProfileById(id);
        return new ResponseEntity<>(profileById, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> updateById(@PathVariable Long id){
        profileService.updatePasswordById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> passwordCheck(@PathVariable Long id, @RequestBody ProfileRequestDto profileRequestDto){
        profileService.checkPassword(id, profileRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
