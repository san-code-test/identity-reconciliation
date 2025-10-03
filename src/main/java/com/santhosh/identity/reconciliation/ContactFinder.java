package com.santhosh.identity.reconciliation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactFinder {
    private final ContactRepository repo;

    public ContactFinder(ContactRepository repo) {
        this.repo = repo;
    }

    public List<Contact> findByPhone(String phone) {
        if(phone == null) return new ArrayList<>();
        return repo.findAllByPhoneNumber(phone);
    }

    public List<Contact> findByEmail(String email) {
        if(email == null) return new ArrayList<>();
        return repo.findAllByEmail(email);
    }

    public List<Contact> findLinkedContacts(int primaryId) {
        return repo.findAllByLinkedId(primaryId);
    }

    public Optional<Contact> findById(int id) {
        return repo.findById(id);
    }
}
