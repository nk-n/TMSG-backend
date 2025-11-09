package ku.cs.tmsg.service;

import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.exception.DriverNotFoundException;
import ku.cs.tmsg.exception.AlreadyLinkedException;
import ku.cs.tmsg.exception.NotFoundException;
import ku.cs.tmsg.repository.DriverRepository;
import ku.cs.tmsg.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LineAuthService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private LineUserDetailsService detailsService;

    @Autowired
    private LineAPIUtil lineAPIUtil;

    @Value("${line.channel-id}")
    private String channelId;

    public Map<String, Object> verifyAndCreateJwt(String idToken) {
        try {
            lineAPIUtil.checkUserID(idToken);
//            System.out.println(body);

            String userId = jwtUtil.getUserIDFromToken(idToken);

            Driver user = driverRepository.findByID(userId);

            // check user
            if (user == null) {
                throw new DriverNotFoundException("User does not exist");
            }

            // set authority
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority("ROLE_DRIVER"));
//
//            UserDetails userDetails = detailsService.loadUserByUsername(userId);
//
//            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//
//
//            SecurityContextHolder.getContext().setAuthentication(auth);


            // issue our own JWT.
            String appJwt = jwtUtil.generateLineToken(user.getLine_ID(), user.getTel());
            return Map.of("jwt", appJwt, "user", userId, "status", "OK");

        } catch (DriverNotFoundException e){
            return Map.of("status", "Error: This LINE ID does not exist in the system");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify LINE token: " + e.getMessage());
        }
    }

    public Map<String, String> linkUser(String idToken, String phoneNumber) {
        try {
            lineAPIUtil.checkUserID(idToken);
            String userId = jwtUtil.getUserIDFromToken(idToken);

            if (driverRepository.isExistsByID(userId)) {
                throw new AlreadyLinkedException("This LINE ID is already linked");
            }

            if (!driverRepository.isExists(phoneNumber)) {
                throw new NotFoundException("This phone number is not registered in the system. Please contact your administrator.");
            }

            Driver driver = driverRepository.findByPhone(phoneNumber);

            if (driver.getLine_ID() != null) {
                throw new AlreadyLinkedException("This User is already linked to a LINE ID");
            }

            driverRepository.updateLineIdByPhone(phoneNumber, userId);
            return  Map.of("token", idToken, "LINE_ID", userId, "PHONE_NUMBER", phoneNumber, "STATUS", "LINKED SUCCESSFUL");
        } catch (AlreadyLinkedException | NotFoundException e) {
            return Map.of("STATUS", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify LINE token: " + e.getMessage());
        }
    }
}

