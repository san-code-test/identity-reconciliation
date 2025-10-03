package com.santhosh.identity.reconciliation;

import java.util.Date;
import java.util.List;

public class ContactLinker {
    private final ContactRepository repo;

    public ContactLinker(ContactRepository repo) {
        this.repo = repo;
    }

    public Contact determinePrimaryContact(IdentityReconciliationRequest request) {
        if (request.getPhoneRecords().isEmpty() && request.getEmailRecords().isEmpty()) {
            return savePrimary(request.getPhone(), request.getEmail());
        }

        if (request.getEmailRecords().isEmpty()) {
            return linkToExisting(request.getPhoneRecords(), request.getPhone(), request.getEmail());
        }

        if (request.getPhoneRecords().isEmpty()) {
            return linkToExisting(request.getEmailRecords(), request.getPhone(), request.getEmail());
        }

        return mergeRecords(request.getPhoneRecords(), request.getEmailRecords());
    }

    private Contact savePrimary(String phone, String email) {
        Contact c = new Contact(phone, email);
        c.setLinkPrecedence(LinkPrecedence.PRIMARY);

        return repo.save(c);
    }

    private Contact linkToExisting(List<Contact> records, String phone, String email) {
        int primaryId = getPrimaryId(records);

        if(phone != null && email != null) {
            Contact c = new Contact(phone, email);
            c.setLinkPrecedence(LinkPrecedence.SECONDARY);
            c.setLinkedId(primaryId);
            repo.save(c);
        }

        return repo.findById(primaryId).orElseThrow();
    }

    private Contact mergeRecords(List<Contact> phoneRecords, List<Contact> emailRecords) {
        int primaryPhoneId = getPrimaryId(phoneRecords);
        int primaryEmailId = getPrimaryId(emailRecords);

        if (primaryPhoneId == primaryEmailId) {
            return repo.findById(primaryPhoneId).orElseThrow();
        }

        Contact phone = repo.findById(primaryPhoneId).orElseThrow();
        Contact email = repo.findById(primaryEmailId).orElseThrow();

        if(phone.getCreatedAt().before(email.getCreatedAt())) {
            updateLinkedIds(emailRecords, primaryPhoneId);
            return phone;
        } 
            
        updateLinkedIds(phoneRecords, primaryEmailId);
        return email;
    }

    private void updateLinkedIds(List<Contact> records, int primaryId) {
        for(Contact record: records) {
            record.setLinkPrecedence(LinkPrecedence.SECONDARY);
            record.setLinkedId(primaryId);
            record.setUpdatedAt(new Date());
            repo.save(record);
        }
    }

    private int getPrimaryId(List<Contact> records) {
        for (Contact r : records) {
            if (r.getLinkPrecedence() == LinkPrecedence.PRIMARY) {
                return r.getId();
            }
            if (r.getLinkedId() != null) {
                return r.getLinkedId();
            }
        }
        throw new IllegalStateException("No primary found in records");
    }
}
