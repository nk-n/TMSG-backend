package ku.cs.tmsg.service;

import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.exception.DriverNotFoundException;
import ku.cs.tmsg.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LineUserDetailsService implements UserDetailsService{

    @Autowired
    private DriverRepository repository;


    @Override
    public UserDetails loadUserByUsername(String ID)
            throws DriverNotFoundException {


        Driver user = repository.findByID(ID);
        if (user == null) {
            throw new DriverNotFoundException("Driver Not Found with ID: " + ID);
        }


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_DRIVER"));


        return new org.springframework.security.core.userdetails.User(
                user.getLine_ID(),
                "",
                authorities
        );
    }
}
