package com.smart.Controller;

import com.smart.dao.IUserDao;
import com.smart.dao.TokenRepository;
import com.smart.helper.Message;
import com.smart.model.PasswordResetToken;
import com.smart.model.User;
import com.smart.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/change-pwd")
public class ForgotPasswordController
{

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/forgot")
    public String forgotPassword()
    {
        System.out.println("Inside controller forgot password");
        return "forgot_password";
    }

    @PostMapping("/process-forgot")
    public String forgotProcess(@RequestParam("email")String email,HttpSession session)
    {
        String output = "";
        System.out.println(email);
        User user = userDao.getUserByUserName(email);
        System.out.println(user);
        if (user != null) {
            output = userDetailsService.sendEmail(user);
        }
        if (output.equals("success")) {
            session.setAttribute("message",new Message("Please Check Your mail..Reset Link sent","alert alert-warning"));
            return "redirect:/forgot?success";
        }
        return "redirect:/forgot?error";
    }

    @GetMapping("/resetPassword/{token}")
    public String resetPasswordForm(@PathVariable String token, Model model,HttpSession session) {
        PasswordResetToken reset = tokenRepository.findByToken(token);
        if (reset != null && userDetailsService.hasExpired(reset.getExpiryDateTime())) {
            model.addAttribute("email", reset.getUser().getEmail());
            return "resetPassword";
        }
        session.setAttribute("message",new Message("Link expired..Generate new link again","alert alert-warning"));
        return "redirect:/forgotPassword?error";
    }

    @PostMapping("/resetPassword")
    public String passwordResetProcess(@RequestParam("email")String email,@RequestParam("password")String password,HttpSession session) {
        User user = userDao.getUserByUserName(email);
        if(user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userDao.save(user);
//            User u = tokenRepository.getByUser(user);

//            tokenRepository.deleteByUser(user);
        }
        System.out.println("Password Changed Successfully");
        session.setAttribute("message",new Message("Password successfully changed..Please continue to login","alert alert-success"));
        return "redirect:/home/login";
    }

}
