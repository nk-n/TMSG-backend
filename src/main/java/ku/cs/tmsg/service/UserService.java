package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.NewUserRequest;
import ku.cs.tmsg.entity.User;
import ku.cs.tmsg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    @Autowired
    public void setAdminRepository(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }


    public void createUser(NewUserRequest request) {
        User user = new User();
        user.setId(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole("ROLE_USER");
        user.setStatus(true);
        user.setPhone(request.getPhone());
        userRepository.save(user);
    }

}
