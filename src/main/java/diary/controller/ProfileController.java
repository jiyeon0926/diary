package diary.controller;

import diary.controller.dto.ProfileRequestDto;
import diary.controller.dto.ProfileResponseDto;
import diary.service.ProfileService;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<Void> updateByIdByPassword(@PathVariable Long id, @RequestBody ProfileRequestDto profileRequestDto){
        profileService.updatePasswordById(id, profileRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> updateById(@PathVariable Long id, @RequestBody ProfileRequestDto profileRequestDto){
        profileService.updateById(id, profileRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
