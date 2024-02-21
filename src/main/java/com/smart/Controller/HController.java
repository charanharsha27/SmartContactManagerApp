package com.smart.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.helper.Message;
import com.smart.model.User;
import com.smart.service.IUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/home")
public class HController
{
	@Autowired
	private IUserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String HomePage(Map<String,Object> map)
	{
		map.put("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/signup")
	public String SignUp(Map<String,Object> map)
	{
		map.put("title","SignUp - Smart Contact Manager");
		map.put("user",new User());
		return "signup";
	}
	
	@PostMapping("/user_register")
	public String registerUser(Map<String,Object> map,@Valid @ModelAttribute("user")User user,BindingResult result,@RequestParam(defaultValue = "false",name = "agreement")boolean agreement,HttpSession session)
	{
		
		
		try {
			
			if(!agreement)
			{
				System.out.println("You haven't accepted the terms and conditions");
				throw new Exception("You haven't accepted the terms and conditions");
			}
			
			if(result.hasErrors())
			{
				System.out.println("Errorss!!");
				map.put("user",user);
				return "signup";
			}
			
			System.out.println(agreement);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setActive(false);
			user.setRole("USER");
			user.setImgUrl("profile_pic.jpg");
			
//			System.out.println(user);
			User user1 = userService.saveUser(user);
			System.out.println(user1);
			session.setAttribute("message",new Message("SignUp Success...Please Continue to Login Page","alert alert-success"));
			map.put("user",user);
			
			return "signup";
		}
		catch(Exception e)
		{
			System.out.println(e);
			session.setAttribute("message",new Message("Something Went Wrong.."+e.getMessage().toString(),"alert alert-danger"));
			map.put("user",user);
			return "signup";
		}
	}
	
	
	@GetMapping("/login")
	public String login()
	{
		return "login";
	}

	@GetMapping("/about")
	public String about()
	{
		return "about";
	}

	@GetMapping("/contact")
	public String contact()
	{
		return "about";
	}

}
