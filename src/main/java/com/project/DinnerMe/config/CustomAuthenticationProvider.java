package com.project.DinnerMe.config;

import com.project.DinnerMe.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private ClientService cs;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        // Manually check if the provided password matches the encoded password in the UserDetails
        if (!passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword())||!userDetails.isEnabled()) {
            throw new BadCredentialsException("Invalid credentials");
        }
        if (cs.isAccountExpired(userDetails.getUsername())) {
            throw new AccountExpiredException("Account has expired");
        }
    }


    @Override
    protected UserDetails retrieveUser(String email, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        return cs.loadUserByUsername(email);
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }
}
