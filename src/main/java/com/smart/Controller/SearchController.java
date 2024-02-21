package com.smart.Controller;

import com.smart.dao.IContactDao;
import com.smart.dao.IUserDao;
import com.smart.model.Contacts;
import com.smart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController
{
    @Autowired
    private IUserDao userDao;

    @Autowired
    private IContactDao contactDao;

    @GetMapping("/contact-search/{text}")
    public ResponseEntity<List<Contacts>> search(@PathVariable("text")String text, Principal principal)
    {
        User user = userDao.getUserByUserName(principal.getName());
        List<Contacts> contacts = contactDao.findByNameContainingAndUser(text,user);
        return ResponseEntity.ok(contacts);
    }
}
