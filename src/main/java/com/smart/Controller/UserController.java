package com.smart.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;

import com.smart.helper.Message;
import com.smart.service.IContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.smart.model.Contacts;
import com.smart.model.User;
import com.smart.service.IUserService;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IContactService contactService;

	private void commonCode(Principal principal,Map<String,Object> map)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		System.out.println(user);
		map.put("user",user);
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Map<String,Object> map,Principal principal)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		System.out.println(user);
		map.put("user",user);
		map.put("title","Dashboard - Smart Contact Manager");
		return "user/dash";
	}
	
	@GetMapping("/add_contact")
	public String AddContactPage(Map<String,Object> map,Principal principal)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		System.out.println(user);
		map.put("user",user);
		map.put("title","AddContact - Smart Contact Manager");
		map.put("contact",new Contacts());
		return "user/add_contact";
	}

	@PostMapping("/process_contact")
	public String processContact(Map<String,Object> map, @ModelAttribute("contact")Contacts contact, Principal principal, @RequestParam(required = false,value = "img") MultipartFile imgUrl, HttpSession session)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		map.put("user",user);
		System.out.println(contact);
//		if(result.hasErrors())
//		{
//			System.out.println("Form has Errors");
//			map.put("contact",contact);
//			return "user/add_contact";
//		}

		try{
			if(imgUrl.isEmpty()){
				contact.setImgUrl("contact.png");
			}
			else{
				user.getContacts().add(contact);
				contact.setImgUrl(imgUrl.getOriginalFilename());
				File file = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file.getAbsolutePath()+File.separator+imgUrl.getOriginalFilename());
				Files.copy(imgUrl.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			contact.setUser(user);

			System.out.println(contact);
			contactService.saveContact(contact);
			System.out.println("File is Uploaded");

			map.put("contact", new Contacts());
			session.setAttribute("message",new Message("Contact Successfully added","alert alert-success"));
			return "user/add_contact";

		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.setAttribute("message",new Message("Something went Wrong..! Please try again...."+e.getMessage(),"alert alert-danger"));
		}

		System.out.println("Control reached here");
		return "user/add_contact";
	}

	@GetMapping("/show-contacts/{page}")
	public String viewContacts(Map<String,Object> map,Principal principal,@PathVariable("page")int page)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		Pageable pageable = PageRequest.of(page, 2);
		Page<Contacts> contacts = contactService.getContactsOfUser(user.getId(),pageable);
		System.out.println(contacts);
		map.put("user",user);
		map.put("contacts",contacts);
		map.put("currPage",page);
		map.put("totalPages",contacts.getTotalPages());
		return "user/show_contacts";
	}

	@GetMapping("view-contact/{id}")
	public String viewContact(@PathVariable("id")int id,Map<String,Object> map,Principal principal)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		Contacts contact = contactService.getContact(id);
		map.put("user",user);

		if(user.getId().equals(contact.getUser().getId()))
			map.put("contact",contact);
		else
			map.put("error", "You are not allowed to Access this page");

		return "user/view_user";
	}

	@GetMapping("/update-contact/{id}")
	public String updateContact(Map<String,Object> map,Principal principal,@PathVariable("id")int id)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		map.put("user",user);
		Contacts contact = contactService.getContact(id);
		map.put("contact",contact);

		return "user/update-contact";
	}

	@PostMapping("/update-contact")
	public String processUpdate(Map<String,Object> map, @ModelAttribute("contact")Contacts contact, Principal principal, @RequestParam(required = false,value = "img") MultipartFile imgUrl, HttpSession session)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		map.put("user",user);
		try {


			if (imgUrl.isEmpty()) {
				Contacts con = contactService.getContact(contact.getId());
				contact.setImgUrl(con.getImgUrl());
				System.out.println("File is Empty");
			} else {
				Contacts con = contactService.getContact(contact.getId());
				//deleting old file
				File delFile = new ClassPathResource("static/img").getFile();
				File dfile = new File(delFile, con.getImgUrl());
				dfile.delete();

				File file = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file.getAbsolutePath() + File.separator + imgUrl.getOriginalFilename());
				Files.copy(imgUrl.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImgUrl(imgUrl.getOriginalFilename());
			}
			contact.setUser(user);
			System.out.println(contact);
			contactService.saveContact(contact);
			session.setAttribute("message",new Message("User successfully updated","alert alert-success"));
			return "user/update-contact";
		}
		catch (Exception e)
		{
			session.setAttribute("message",new Message("Something went wrong"+e.getMessage(),"alert alert-danger"));
			e.printStackTrace();
		}

		return "user/update-contact";
	}

	@GetMapping("/delete-contact/{id}")
	public String deleteContact(Map<String,Object> map,Principal principal,@PathVariable("id")int id,HttpSession session)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		Contacts contact = contactService.getContact(id);
		user.getContacts().remove(contact);
		if(contact.getUser().getId() == user.getId()){
			contactService.deleteContact(contact);
			session.setAttribute("message",new Message("Contact Successfully deleted","alert alert-success"));
		}
		else{
			map.put("error","Ypu are not allowed to access this page");
		}
		map.put("user",user);
		return "redirect:/user/show-contacts/0";
	}

	@GetMapping("/settings")
	public String Settings(Principal principal,Map<String,Object> map)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		map.put("user",user);
		return "user/profile";
	}

	@GetMapping("/change-password")
	public String changePwd(Principal principal,Map<String,Object> map)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		map.put("user",user);

		return "user/change-pwd1";
	}

	@PostMapping("/change-password")
	public String changePassword(Principal principal,Map<String,Object> map,@RequestParam("oldpwd")String oldpwd,@RequestParam("newpwd")String newpwd,HttpSession session)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		map.put("user",user);

		System.out.println(oldpwd+" "+newpwd);
		System.out.println(user.getPassword());
		Boolean status = userService.changePassword(oldpwd,user.getPassword(),newpwd,user);

		if(status.equals(true)) {
			System.out.println("Password change success");
			session.setAttribute("message",new Message("Password change success","alert alert-success"));
		}
		else{
			System.out.println("Password change not success");
			session.setAttribute("message",new Message("Old passwords dont match","alert alert-danger"));
		}

		return "user/change-pwd1";
	}

	@GetMapping("/contribute")
	public String contribute(Map<String,Object> map,Principal principal)
	{
		String userName = principal.getName();
		User user = userService.getData(userName);
		map.put("user",user);

		return "user/contribute";
	}

}
