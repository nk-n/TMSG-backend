package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.request.NewUserRequest;
import ku.cs.tmsg.dto.response.UserResponse;
import ku.cs.tmsg.entity.User;
import ku.cs.tmsg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<UserResponse> getAll() {
        try {
            List<User> users = userRepository.findAll();
            List<UserResponse> userResponses = new ArrayList<>();
            for (User user : users) {
                UserResponse userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setName(user.getName());
                userResponse.setPhone(user.getPhone());
                userResponses.add(userResponse);
            }
            return userResponses;
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage()){};
        }
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

    public UserResponse delete(String id) {
        try {
            User delUser = userRepository.softDelete(id);
            UserResponse userResponse = new UserResponse();
            userResponse.setId(delUser.getId());
            userResponse.setName(delUser.getName());
            userResponse.setPhone(delUser.getPhone());
            return  userResponse;
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage()){};
        }
    }

}
