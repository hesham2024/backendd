package com.project.DinnerMe.services;

import com.project.DinnerMe.config.CustomUserDetails;
import com.project.DinnerMe.entity.*;
import com.project.DinnerMe.event.RegistrationCompleteEvent;
import com.project.DinnerMe.model.UserData;
import com.project.DinnerMe.model.UserModel;
import com.project.DinnerMe.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

@Service

public class ClientService implements UserDetailsService {
    @Autowired
    private ClientRepository cr;

    @Autowired
    private StaffRepository sr;
    @Autowired
    private verificationTokenRepository v;

    @Autowired
    private passwordResetTokenRepository p;

    @Autowired
    private PasswordEncoder pe;


    private final JavaMailSender javaMailSender;
    @Autowired
    private favRestRepository fr;

    @Bean
    public UserDetailsService userDetailsService() {
        // Your implementation here
        return new ClientService(javaMailSender);
    }

    public ClientService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String text,String text2) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        String text1 = "to verify the account please click on the following link: <a href= "+text+">verify</a> and if the link doesn't activate the email click on the following link to resend email: <a href= "+text2+">resend</a>";
        try {
            helper = new MimeMessageHelper(message, true);


            helper.setTo(to);
            helper.setSubject("Validate email");
            helper.setText(text1, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(message);
    }
    public void sendResetEmail(String to, String text) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        String text1 = "click here to change your password "+text;
        try {
            helper = new MimeMessageHelper(message, true);


            helper.setTo(to);
            helper.setSubject("Password Reset");
            helper.setText(text1, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(message);
    }

    public Client registerUser(UserModel userModel) {
        Client c = new Client();
        Client user = new Client();
        user.setEmail(userModel.getEmail());
        user.setFullName(userModel.getFirstName() + " " + userModel.getLastName());
        user.setPassword(pe.encode(userModel.getPassword()));
        user.setPhoneNumber(userModel.getPhoneNumber());
        user.setExpirationDate(LocalDate.now().plusYears(1));
        user.setAddress(userModel.getAddress());
        user.setBirthdate(userModel.getBirthdate());
        user.setPhotoFilePath(userModel.getPhotoFilePath()==null ? null : userModel.getPhotoFilePath());
        c = findUserByEmail(userModel.getEmail());
        if (c == null) {
            cr.save(user);
            return user;
        } else {
            return null;
        }

    }

    public void saveVerificationTokenForUser(String token, Client user) {
        VerificationToken verificationToken
                = new VerificationToken(user, token);

        v.save(verificationToken);
    }

    public String validateVerificationToken(String token) {
        VerificationToken verificationToken
                = v.findByToken(token);

        if (verificationToken == null) {
            return "invalid";
        }

        Client user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            v.delete(verificationToken);
            return "expired";
        }

        user.setEnable(true);
        cr.save(user);
        v.delete(verificationToken);
        return "valid";
    }

    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken
                = v.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        v.save(verificationToken);
        return verificationToken;
    }

    public Client findUserByEmail(String email) {
        return cr.findByEmail(email);
    }

    public Staff findStaffByEmail(String email) {
        return sr.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(Client user, String token) {
        PasswordResetToken passwordResetToken
                = new PasswordResetToken(user, token);
        p.save(passwordResetToken);
    }

    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken
                = p.findByToken(token);

        if (passwordResetToken == null) {
            return "invalid";
        }

        Client user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            p.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    public Optional<Client> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(p.findByToken(token).getUser());
    }

    public void changePassword(Client user, String newPassword) {
        user.setPassword(pe.encode(newPassword));
        cr.save(user);
    }

    public boolean checkIfValidOldPassword(Client user, String oldPassword) {
        return pe.matches(oldPassword, user.getPassword());
    }

    public boolean isAccountExpired(String email) {
        Client c = cr.findByEmail(email);
        return !c.getExpirationDate().isAfter(LocalDate.now());
    }
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = findUserByEmail(email);
        Staff staff = findStaffByEmail(email);
        CustomUserDetails userDetails;

        if (staff == null && client == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        } else if (staff != null && client == null) {
            userDetails = new CustomUserDetails(staff.getEmail(), staff.getPassword(), staff.getRole(),staff.getEnable());
        } else if (staff == null && client != null) {
            userDetails = new CustomUserDetails(client.getEmail(), client.getPassword(), client.getRole(), client.getEnable());
        } else {
            // Both Staff and Client exist, prompt the user to choose
            userDetails = askUserToChooseRole(client, staff);
        }

        return userDetails;
    }

    private CustomUserDetails askUserToChooseRole(Client client, Staff staff) {
        // You need to implement logic to prompt the user to choose the role
        // This could involve presenting options to the user and getting their input
        // For example, you might use a dialog or console input

        // For this example, let's assume a hypothetical method getRoleChoiceFromUser()
        String chosenRole = getRoleChoiceFromUser();

        if (chosenRole.equals("USER")) {
            return new CustomUserDetails(client.getEmail(), client.getPassword(), client.getRole(),client.getEnable());
        } else {
            return new CustomUserDetails(staff.getEmail(), staff.getPassword(), staff.getRole(), staff.getEnable());
        }
    }

    private String getRoleChoiceFromUser() {
        // Implement logic to get the role choice from the user
        // This could be through user input, a UI, or any other mechanism
        // For this example, let's assume the user chooses a role through console input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose role: 1 for User, 2 for Staff");
        int choice = scanner.nextInt();

        return (choice == 1) ? "USER": (choice == 2) ? "STAFF" : "ADMIN";
    }
    public Client getUserByToken(String oldToken) {

        if(v.findByToken(oldToken)==null)
            return null;
        else
         return v.findByToken(oldToken).getUser();
    }


    public String updateUser(UserData ud, String email1) {
        Client c = findUserByEmail(email1);
        if (c != null) {
            // Create UserData object
            UserData userData = new UserData();
            c.setFullName(ud.getFullName());
            c.setPhoneNumber(ud.getPhoneNumber());
            c.setEmail(ud.getEmail());
            c.setAddress(ud.getAddress());
            c.setPhotoFilePath(ud.getPhotoFilePath());
            c.setBirthdate(ud.getBirthdate());
            if (email1.equalsIgnoreCase(c.getEmail())) {
                cr.save(c);
            }
            else {
                c.setEnable(false);
                cr.save(c);
            }
            return "Data Changed";
        } else {
            return "error occurred";
        }
    }

    public List<favrest> getfavrests(String id) {
        return fr.findByClientId(Long.parseLong(id));
    }
}