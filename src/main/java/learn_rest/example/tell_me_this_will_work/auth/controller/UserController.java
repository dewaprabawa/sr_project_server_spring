package learn_rest.example.tell_me_this_will_work.auth.controller;

import learn_rest.example.tell_me_this_will_work.auth.model.User;
import learn_rest.example.tell_me_this_will_work.auth.payload.request.response.UserResponse;
import learn_rest.example.tell_me_this_will_work.auth.repository.UserRepository;
import learn_rest.example.tell_me_this_will_work.auth.security.JwtUtils;
import learn_rest.example.tell_me_this_will_work.auth.service.UserDetailsImpl;
import learn_rest.example.tell_me_this_will_work.firebaseConfig.FirebaseConfig;
import learn_rest.example.tell_me_this_will_work.helper.FinalResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://127.0.0.1:8080")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private FirebaseConfig firebaseFileService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        ResponseEntity<UserResponse> ok = ResponseEntity.ok(new UserResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
        return ok;
    }


    @PostMapping("/uploadImageProfile")
    public ResponseEntity<FinalResult<?>> create(@RequestParam(name = "file") MultipartFile file) throws FailedUploadImageException {
        try {
            String fileName = firebaseFileService.uploadFileToStorage(file);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            if (user.isPresent()) {
                user.get().setProfileImageUrl(fileName);
                userRepository.save(user.get());
                String message = "User profile image uploaded successfully";
                String statusCode = "UPLOAD_IMAGE_SUCCESS";
                var finalResult = new FinalResult(true, message, statusCode, user);
                return new ResponseEntity<>(finalResult, HttpStatus.CREATED);
            } else {
                String message = "User not found";
                String statusCode = "USER_NOT_FOUND";
                var finalResult = new FinalResult(false, message, statusCode, null);
                return new ResponseEntity<>(finalResult, HttpStatus.NOT_FOUND);
            }
            // do whatever you want with that
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new FailedUploadImageException(e.toString());
            //  throw internal error;
        }
    }

}


class FailedUploadImageException extends Exception {
    String message;

    FailedUploadImageException(
            String message
    ) {
        this.message = message;
    }
}