package com.project.DinnerMe.controller;

import com.project.DinnerMe.entity.Client;
import com.project.DinnerMe.entity.VerificationToken;
import com.project.DinnerMe.event.RegistrationCompleteEvent;
import com.project.DinnerMe.model.PasswordModel;
import com.project.DinnerMe.model.UserModel;
import com.project.DinnerMe.services.ClientService;
import com.project.DinnerMe.services.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ReservationController {
    @Autowired
    ClientService cs;


    @Autowired
    StaffService ss;
    @Autowired
    private ApplicationEventPublisher publisher;
    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request)  {
        Client user = cs.registerUser(userModel);
        if (user!=null){
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "Success";
    }else{
     return "this user exist";
        }
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        Client c = cs.getUserByToken(token);
        System.out.println(token);
        if (c == null) {
            return "this user is enabled";
        } else {
            String result = cs.validateVerificationToken(token);
            if (result.equalsIgnoreCase("valid")) {
                return "User Verified Successfully";
            }
            return "Bad User";
        }
    }


    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          HttpServletRequest request) {
        Client c =cs.getUserByToken(oldToken);

        if (c == null) {
            return "this user is enabled";
        } else {
            VerificationToken verificationToken
                    = cs.generateNewVerificationToken(oldToken);
        Client user = verificationToken.getUser();
        String url1 =
                applicationUrl(request)
                        + "/verifyRegistration?token="
                        + verificationToken.getToken();
        String url2 =
                applicationUrl(request)
                        + "/resendVerifyToken?token="
                        + verificationToken.getToken();

        cs.sendEmail(user.getEmail(), url1,url2);
        return "Verification Link Sent";
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        Client user = cs.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user!=null) {
            String token = UUID.randomUUID().toString();
            cs.createPasswordResetTokenForUser(user,token);
            url =applicationUrl(request)+"/savePassword?token="+token;
             cs.sendResetEmail(user.getEmail(),url);
            return "email sent";
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel) {
        String result = cs.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")) {
            return "Invalid Token";
        }
        Optional<Client> user = cs.getUserByPasswordResetToken(token);
        if(user.isPresent()) {
            cs.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password Reset Successfully";
        } else {
            return "Invalid Token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        Client user = cs.findUserByEmail(passwordModel.getEmail());
        if(!cs.checkIfValidOldPassword(user,passwordModel.getOldPassword())) {
            return "Invalid Old Password";
        }
        //Save New Password
        cs.changePassword(user,passwordModel.getNewPassword());
        return "Password Changed Successfully";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort();
    }
}
