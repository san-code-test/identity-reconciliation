package com.santhosh.identity.reconciliation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IdentityReconciliationServiceTests {

    @Mock
    private ContactRepository contactRepoMock;

    @InjectMocks
    private IdentityReconciliationService reconService;

    private static String inputPhoneNumber;
    private static String inputEmail;

    @BeforeAll
    private static void setup() {
        inputPhoneNumber = "1234567890";
        inputEmail = "test@example.com";
    }

    @Test
    void testDoIdentityReconciliation_noMatches() {
        when(contactRepoMock.findAllByPhoneNumber(inputPhoneNumber)).thenReturn(new ArrayList<>());
        when(contactRepoMock.findAllByEmail(inputEmail)).thenReturn(new ArrayList<>());

        Contact savedContact = new Contact(inputPhoneNumber, inputEmail);
        savedContact.setId(1);
        when(contactRepoMock.save(any(Contact.class))).thenReturn(savedContact);

        String res = reconService.reconcileIdentity(inputPhoneNumber, inputEmail);
        assertNotNull(res);
        assertThat(res).contains(inputEmail);
        assertThat(res).contains(inputPhoneNumber);
        assertThat(res).contains("\"primaryContactId\":1");
    }
}
