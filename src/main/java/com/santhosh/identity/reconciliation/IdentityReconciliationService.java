package com.santhosh.identity.reconciliation;

import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class IdentityReconciliationService {
    private final ContactRepository contactRepository;
    private final ContactFinder contactFinder;
    private final ContactLinker contactLinker;
    private final IdentityResponseBuilder responseBuilder;


    public IdentityReconciliationService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
        this.contactFinder = new ContactFinder(contactRepository);
        this.contactLinker = new ContactLinker(contactRepository);
        this.responseBuilder = new IdentityResponseBuilder(contactRepository);
    }

    public String reconcileIdentity(String phoneNumber, String email) {
        List<Contact> phoneRecords = contactFinder.findByPhone(phoneNumber);
        List<Contact> emailRecords = contactFinder.findByEmail(email);

        Contact primaryContact = contactLinker.determinePrimaryContact(
            new IdentityReconciliationRequest(phoneNumber, email, phoneRecords, emailRecords));

        return responseBuilder.build(primaryContact);
    }
}
