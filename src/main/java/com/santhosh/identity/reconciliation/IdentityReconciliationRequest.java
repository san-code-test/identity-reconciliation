package com.santhosh.identity.reconciliation;

import java.util.List;

import lombok.Getter;

@Getter
public class IdentityReconciliationRequest {
    private final String phone;
    private final String email;
    private final List<Contact> phoneRecords;
    private final List<Contact> emailRecords;

    public IdentityReconciliationRequest(String phone, String email,
                           List<Contact> phoneRecords,
                           List<Contact> emailRecords) {
        this.phone = phone;
        this.email = email;
        this.phoneRecords = phoneRecords;
        this.emailRecords = emailRecords;
    }
}
