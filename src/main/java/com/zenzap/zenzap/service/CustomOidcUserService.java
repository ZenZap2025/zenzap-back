package com.zenzap.zenzap.service;

import com.zenzap.zenzap.entity.User;
import com.zenzap.zenzap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String googleId = oidcUser.getSubject();

        Optional<User> existingUser = userRepository.findByEmailAddress(email);

        User user = existingUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmailAddress(email);
            newUser.setUsername(name);
            newUser.setProvider("google");
            newUser.setProviderId(googleId);
            newUser.setIsActive("true");
            newUser.setTypeUser("USER");
            return newUser;
        });

        System.out.println("✅ Guardando nuevo usuario: " + user);
        userRepository.save(user);

        return oidcUser;
    }
}
