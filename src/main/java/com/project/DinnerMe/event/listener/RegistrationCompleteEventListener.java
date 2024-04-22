package com.project.DinnerMe.event.listener;
import com.project.DinnerMe.entity.Client;
import com.project.DinnerMe.event.RegistrationCompleteEvent;
import com.project.DinnerMe.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private ClientService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the Verification Token for the User with Link
        Client user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);
        //Send Mail to user
        String url1 =
                event.getApplicationUrl()
                        + "/verifyRegistration?token="
                        + token;
        String url2 =
                event.getApplicationUrl()
                        + "/resendVerifyToken?token="
                        + token;

    userService.sendEmail(user.getEmail(),url1,url2);
    }
}
