package com.smart.service;

import com.smart.dao.IContactDao;
import com.smart.model.Contacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IContactServiceImpl implements IContactService{

    @Autowired
    private IContactDao contactDao;
    @Override
    public Contacts saveContact(Contacts contact) {
        return contactDao.save(contact);
    }

    @Override
    public Page<Contacts> getContactsOfUser(int id, Pageable pageable) {
        return contactDao.getContactsByUserId(id,pageable);
    }

    @Override
    public Contacts getContact(int id) {
        if(contactDao.findById(id).isPresent())
            return contactDao.findById(id).get();
        return null;
    }

    @Override
    public void deleteContact(Contacts contact) {
        contactDao.delete(contact);
        System.out.println("Contact deleted");
    }


}
