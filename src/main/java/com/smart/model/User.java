package com.smart.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Name should not be null")
	@Size(min = 3,max = 20,message = "Length of name must be in-between 3 to 20 characters")
	private String name;

	@Column(unique = true)
	@NotBlank(message = "Email Should Not be Null")
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid Email Address Format")
	private String email;

//	@Size(min = 8,max = 15,message = "Password Should be in the specified format")
	@NotBlank(message = "Password Should Not be Null")
	private String password;

	private String imgUrl;
	private String role;

	private boolean isActive;

	@Column(length = 1000)
	@NotBlank(message = "About Filed Should not be null")
	@Size(min = 10,max = 50,message = "Length of About Field must be in-between 10 to 50 characters")
	private String uabout;

	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Contacts> contacts = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<PasswordResetToken> passwordResetToken;


}
