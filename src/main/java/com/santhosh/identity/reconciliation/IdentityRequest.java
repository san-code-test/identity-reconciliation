package com.santhosh.identity.reconciliation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentityRequest {
    private String phoneNumber;
    private String email;
}
