package com.smart.dao;

import com.smart.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.model.Contacts;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IContactDao extends JpaRepository<Contacts, Integer>
{
    @Query(value = "select * from contacts c where c.user_id=:id",nativeQuery = true)
    public Page<Contacts> getContactsByUserId(int id, Pageable pageable);

    public List<Contacts> findByNameContainingAndUser(String name, User user);
}
