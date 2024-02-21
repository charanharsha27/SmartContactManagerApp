package com.smart.service;

import com.smart.model.Contacts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IContactService {

    public Contacts saveContact(Contacts contact);

    public Page<Contacts> getContactsOfUser(int id, Pageable pageable);

    public Contacts getContact(int id);

    public void deleteContact(Contacts contact);
}
