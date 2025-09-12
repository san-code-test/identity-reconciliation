package com.santhosh.identity.reconciliation;

import org.springframework.data.repository.CrudRepository;
import java.util.List;



public interface ContactRepository extends CrudRepository<Contact, Integer> {
    List<Contact> findAllByPhoneNumber(String phoneNumber);
    List<Contact> findAllByEmail(String email);
    List<Contact> findAllByLinkedId(Integer linkedId);
}
