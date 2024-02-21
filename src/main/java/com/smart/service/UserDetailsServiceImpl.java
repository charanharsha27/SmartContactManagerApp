package com.smart.service;

import com.smart.dao.TokenRepository;
import com.smart.model.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.smart.config.UserDetailsImpl;
import com.smart.dao.IUserDao;
import com.smart.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private IUserDao dao;

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	TokenRepository tokenRepository;

	@Value("${spring.mail.username}")
	private String fromMail;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		try {
			System.out.println(username);
			User user =  dao.getUserByUserName(username);
			if (user == null) {
				throw new UsernameNotFoundException("User not found");
			}
			System.out.println("Data retreived");
			return new UserDetailsImpl(user);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return null;
		
	}

	public String sendEmail(User user) {
		try {
			String resetLink = generateResetToken(user);

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom(fromMail);
			msg.setTo(user.getEmail());

			msg.setSubject("Hello "+user.getName());
			msg.setText("\n\n" + "Please click on this link to Reset your Password :" + resetLink + ". \n\n"
					+ "Regards \n" + "ABC");

			javaMailSender.send(msg);

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}


	public String generateResetToken(User user) {
		UUID uuid = UUID.randomUUID();
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime expiryDateTime = currentDateTime.plusMinutes(5);
		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setUser(user);
		resetToken.setToken(uuid.toString());
		resetToken.setExpiryDateTime(expiryDateTime);
		resetToken.setUser(user);
		PasswordResetToken token = tokenRepository.save(resetToken);
		if (token != null) {
			System.out.println("Mail sent");
			String endpointUrl = "http://localhost:9090/smartApp/change-pwd/resetPassword";
			return endpointUrl + "/" + resetToken.getToken();
		}
		return "";
	}

	public boolean hasExpired(LocalDateTime expiryDateTime) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		return expiryDateTime.isAfter(currentDateTime);
	}
}
