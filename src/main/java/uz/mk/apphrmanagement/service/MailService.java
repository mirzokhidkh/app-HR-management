package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.payload.ApiResponse;

import java.util.Optional;

@Service
public class MailService {
    @Autowired
    JavaMailSender javaMailSender;


    public Boolean sendEmail(String sendingEmail, String text) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("example@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Confirm Account");
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
