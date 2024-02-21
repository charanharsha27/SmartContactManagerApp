package com.smart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
public class Contacts 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "Name Field Should not be Null")
	private String name;

	@Column(unique = true)
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid Email Address Format")
	private String email;

	@NotBlank(message = "Field Should not be Null")
	private String nickName;

	@NotBlank(message = "About Field Should not be Null")
	private String about;


	private String imgUrl;
	
	@Column(unique = true)
	@Digits(integer = 10,fraction = 0,message = "Number must be in specified format")
	private long number;
	
	@ManyToOne
	@JsonIgnore
	private User user;

	@Override
	public String toString() {
		return "Contacts [id=" + id + ", name=" + name + ", email=" + email + ", nickName=" + nickName + ", about="
				+ about + ", number=" + number + "]";
	}

	@Override
	public boolean equals(Object obj)
	{
		return this.id == ((Contacts)obj).getId();
	}


}
