package learn_rest.example.tell_me_this_will_work.auth.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.util.JSONPObject;
import learn_rest.example.tell_me_this_will_work.auth.model.Role;
import learn_rest.example.tell_me_this_will_work.auth.model.User;
import learn_rest.example.tell_me_this_will_work.auth.payload.request.response.UserResponse;
import learn_rest.example.tell_me_this_will_work.helper.FinalResult;
import learn_rest.example.tell_me_this_will_work.helper.ERole;
import learn_rest.example.tell_me_this_will_work.auth.payload.request.LoginRequest;
import learn_rest.example.tell_me_this_will_work.auth.payload.request.SignupRequest;
import learn_rest.example.tell_me_this_will_work.auth.repository.UserRepository;
import learn_rest.example.tell_me_this_will_work.auth.repository.RoleRepository;
import learn_rest.example.tell_me_this_will_work.auth.security.JwtUtils;
import learn_rest.example.tell_me_this_will_work.auth.service.UserDetailsImpl;
import org.bson.json.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author I DEWA GEDE AGUS PRABAWA
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signIn")
    public ResponseEntity<FinalResult<UserResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        System.out.println("USERNAME " + loginRequest.getUsername());
        System.out.println("PASSWORD " + loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        String message = "successfully logged in";
        String statusCode = "USER_SUCCESS_LOGGED_IN";

        var result = new FinalResult(
                true,
                message,
                statusCode,
                new UserResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        userDetails.getProfileImageUrl(),
                        userDetails.getCountryCode(),
                        userDetails.getPhoneNumber()
                ));

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout"; //You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

    @PostMapping("/signUp")
    public ResponseEntity<FinalResult> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        System.out.println("USERNAME" + signUpRequest.getUsername());
        System.out.println("EMAIL" + signUpRequest.getEmail());
        System.out.println("PASSWORD" + signUpRequest.getPassword());

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            boolean isSuccess = false;
            String statusCode = "Username_ALREADY_USED";
            String message = "Username is already taken!";

            FinalResult<String> finalResult = new FinalResult(isSuccess, statusCode, message, null);
            return ResponseEntity
                    .badRequest()
                    .body(finalResult);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            boolean isSuccess = false;
            String statusCode = "EMAIL_ALREADY_USED";
            String message = "Email is already taken!";

            FinalResult<String> finalResult = new FinalResult<String>(isSuccess, statusCode, message, null);
            return ResponseEntity
                    .badRequest()
                    .body(finalResult);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getCountryCode(),
                signUpRequest.getPhoneNumber()
        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        boolean isSuccess = true;
        String statusCode = "REGISTER_SUCCESSFULLY";
        String message = "User registered successfully!";

        FinalResult<String> finalResult = new FinalResult<String>(isSuccess, statusCode, message, null);

        return ResponseEntity.ok(finalResult);
    }

}
