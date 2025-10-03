package com.santhosh.identity.reconciliation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class IdentityResponseBuilder {
    private final ContactRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public IdentityResponseBuilder(ContactRepository repo) {
        this.repo = repo;
    }

    public String build(Contact primaryContact) {
        List<Contact> linkedContacts = repo.findAllByLinkedId(primaryContact.getId());

        Set<String> phoneNumbers = new HashSet<>();
        Set<String> emails = new HashSet<>();
        Set<Integer> secondaryIds = new HashSet<>();
        
        if(primaryContact.getPhoneNumber() != null)
        phoneNumbers.add(primaryContact.getPhoneNumber());

        if(primaryContact.getEmail() != null)
        emails.add(primaryContact.getEmail());

        phoneNumbers.addAll(linkedContacts.stream().map(Contact::getPhoneNumber).filter(Objects::nonNull).toList());
        emails.addAll(linkedContacts.stream().map(Contact::getEmail).filter(Objects::nonNull).toList());
        secondaryIds.addAll(linkedContacts.stream().map(Contact::getId).filter(Objects::nonNull).toList());

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("primaryContactId", primaryContact.getId());
        jsonMap.put("emails", emails);
        jsonMap.put("phoneNumbers", phoneNumbers);
        jsonMap.put("secondaryContactIds", secondaryIds);

        try {
            return mapper.writeValueAsString(jsonMap);
        } catch (Exception e) {
            throw new RuntimeException("Error converting contacts to JSON", e);
        }
    }
}

