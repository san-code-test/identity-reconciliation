package com.santhosh.identity.reconciliation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {
  @Autowired
  private ContactRepository contactRepository;

  @PostMapping(path="/identity") 
  public @ResponseBody String doIdentityReconciliation (@RequestParam String phoneNumber
      , @RequestParam String email) {
    List<Contact> phoneRecords = contactRepository.findAllByPhoneNumber(phoneNumber);
    List<Contact> emailRecords = contactRepository.findAllByEmail(email);

    int primaryContactId=-1; 
    
    if(phoneRecords.isEmpty() && emailRecords.isEmpty()) {

      Contact contact = new Contact(phoneNumber, email);
      contact.setLinkPrecedence(LinkPrecedence.PRIMARY);
      contactRepository.save(contact);
      primaryContactId = contact.getId();

    } else if(emailRecords.isEmpty()) {
      for(Contact phoneRecord: phoneRecords) {
        if(phoneRecord.getLinkPrecedence()==LinkPrecedence.PRIMARY) {
          primaryContactId = phoneRecord.getId();
        }
        
        if(phoneRecord.getLinkedId()!=null) {
          primaryContactId = phoneRecord.getLinkedId();
        }
      }

      Contact contact = new Contact(phoneNumber, email);
      contact.setLinkPrecedence(LinkPrecedence.SECONDARY);
      contact.setLinkedId(primaryContactId);
      contactRepository.save(contact);

      
    } else if(phoneRecords.isEmpty()) {
      for(Contact emailRecord: emailRecords) {
        if(emailRecord.getLinkPrecedence()==LinkPrecedence.PRIMARY) {
          primaryContactId = emailRecord.getId();
        }
        
        if(emailRecord.getLinkedId()!=null) {
          primaryContactId = emailRecord.getLinkedId();
        }
      }

      Contact contact = new Contact(phoneNumber, email);
      contact.setLinkPrecedence(LinkPrecedence.SECONDARY);
      contact.setLinkedId(primaryContactId);
      contactRepository.save(contact);

    } else {
      int primaryEmailContactId=-1;
      int primaryPhoneContactId=-1;

      for(Contact phoneRecord: phoneRecords) {
        if(phoneRecord.getLinkPrecedence()==LinkPrecedence.PRIMARY) {
          primaryPhoneContactId = phoneRecord.getId();
        }
        
        if(phoneRecord.getLinkedId()!=null) {
          primaryPhoneContactId = phoneRecord.getLinkedId();
        }
      }

      for(Contact emailRecord: emailRecords) {
          if(emailRecord.getLinkPrecedence()==LinkPrecedence.PRIMARY) {
            primaryEmailContactId = emailRecord.getId();
          }
          
          if(emailRecord.getLinkedId()!=null) {
            primaryEmailContactId = emailRecord.getLinkedId();
          }
      }

      if(primaryEmailContactId!=primaryPhoneContactId) {
        Contact primaryEmailContact = contactRepository.findById(primaryEmailContactId).get();
        Contact primaryPhoneContact = contactRepository.findById(primaryPhoneContactId).get();

        Contact oldestContact = (primaryEmailContact.getCreatedAt().before(primaryPhoneContact.getCreatedAt()))?primaryEmailContact:primaryPhoneContact;
        Contact secondaryContact = (primaryEmailContact.equals(oldestContact))?primaryPhoneContact:primaryEmailContact;
        primaryContactId = oldestContact.getId();

        secondaryContact.setLinkPrecedence(LinkPrecedence.SECONDARY);
        secondaryContact.setLinkedId(primaryContactId);
        secondaryContact.setUpdatedAt(new Date());

        contactRepository.save(secondaryContact);
      } else {
        primaryContactId = primaryEmailContactId;
      }
    }

    List<String> linkedPhoneNumbers = new ArrayList<>();
    List<String> linkedEmails = new ArrayList<>();
    List<Integer> secondaryIds = new ArrayList<>();

    Contact primaryContact = contactRepository.findById(primaryContactId).get();
    linkedPhoneNumbers.add(primaryContact.getPhoneNumber());
    linkedEmails.add(primaryContact.getEmail());

    List<Contact> linkedContacts = contactRepository.findAllByLinkedId(primaryContactId);

    linkedPhoneNumbers.addAll(linkedContacts.stream().map(Contact::getPhoneNumber).toList());
    linkedEmails.addAll(linkedContacts.stream().map(Contact::getEmail).toList());
    secondaryIds.addAll(linkedContacts.stream().map(Contact::getId).toList());
    
    try {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("primaryContactId", primaryContactId);
        jsonMap.put("emails", linkedEmails);
        jsonMap.put("phoneNumbers", linkedPhoneNumbers);
        jsonMap.put("secondaryContactIds", secondaryIds);
      String json = mapper.writeValueAsString(
        jsonMap
      );
      return json;
    } catch (Exception e) {
      return "Error converting contacts to JSON";
    }
  }
}
