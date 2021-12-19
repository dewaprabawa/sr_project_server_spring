package learn_rest.example.tell_me_this_will_work.auth.controller;

import learn_rest.example.tell_me_this_will_work.auth.model.User;
import learn_rest.example.tell_me_this_will_work.auth.payload.request.response.UserResponse;
import learn_rest.example.tell_me_this_will_work.auth.repository.UserRepository;
import learn_rest.example.tell_me_this_will_work.auth.security.JwtUtils;
import learn_rest.example.tell_me_this_will_work.auth.service.UserDetailsImpl;
import learn_rest.example.tell_me_this_will_work.firebaseConfig.FirebaseConfig;
import learn_rest.example.tell_me_this_will_work.helper.FinalResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    @GetMapping(value = "/user")
    public ResponseEntity<FinalResult> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (user.isPresent()) {
            System.out.println(user.get().getId());
            System.out.println(user.get().getId());
            System.out.println(user.get().getUsername());
            System.out.println(user.get().getEmail());
            System.out.println(user.get().getCountryCode());
            System.out.println(user.get().getPhoneNumber());

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            String message = "Successfully get current user";
            String statusCode = "CURRENT_USER_SUCCESS";
            String jwt = jwtUtils.generateJwtToken(authentication);

            return new ResponseEntity<>(new FinalResult(true, message, statusCode, new UserResponse(jwt,
                    user.get().getId(),
                    user.get().getUsername(),
                    user.get().getEmail(),
                    roles,
                    user.get().getProfileImageUrl(),
                    user.get().getCountryCode(),
                    user.get().getPhoneNumber()
                    )), HttpStatus.OK);
        } else {
            String message = "Failed get current user";
            String statusCode = "USER_NOT_FOUND";
            boolean success = false;
            var finalResult = new FinalResult(
                    success,
                    statusCode,
                    message,
                    null
            );
            return new ResponseEntity<>(finalResult, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/user/uploadImageProfile")
    public ResponseEntity<FinalResult<?>> create(@RequestParam(name = "file") MultipartFile file) throws FailedUploadImageException {
        try {
            String fileName = firebaseFileService.save(file);
            String imageUrl = firebaseFileService.getImageUrl(fileName);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            if (user.isPresent()) {
                user.get().setProfileImageUrl(imageUrl);
                userRepository.save(user.get());
                String message = "User profile image uploaded successfully";
                String statusCode = "UPLOAD_IMAGE_SUCCESS";
                var uploadedProfilePicture = user.get().getProfileImageUrl();
                var finalResult = new FinalResult(true, message, statusCode, uploadedProfilePicture);
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

