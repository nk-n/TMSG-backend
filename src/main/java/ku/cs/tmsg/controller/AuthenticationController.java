package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.request.LoginRequest;
import ku.cs.tmsg.dto.request.NewUserRequest;
import ku.cs.tmsg.security.JwtUtil;
import ku.cs.tmsg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;


    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    JwtUtil jwtUtils;

    @PostMapping("/new-user")
    public String registerUser(@RequestBody NewUserRequest request) {


        if (userService.userExists(request.getUsername()))
            return "Error: Username is already taken!";


        userService.createUser(request);
        return "User registered successfully!";
    }


    @PostMapping("/signin")
    public String authenticateUser(@RequestBody LoginRequest request) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }

}


