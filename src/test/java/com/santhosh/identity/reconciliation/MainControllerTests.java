package com.santhosh.identity.reconciliation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MainControllerTests {

    @Mock
    private ContactRepository contactRepoMock;

    @InjectMocks
    private MainController mainController;

    private static String inputPhoneNumber;
    private static String inputEmail;

    @BeforeAll
    private static void setup() {
        inputPhoneNumber = "1234567890";
        inputEmail = "test@example.com";
    }

    @Test
    void testDoIdentityReconciliation_NoMatches() {
        when(contactRepoMock.findAllByPhoneNumber(inputPhoneNumber)).thenReturn(new ArrayList<>());
        when(contactRepoMock.findAllByEmail(inputEmail)).thenReturn(new ArrayList<>());

        Contact savedContact = new Contact(inputPhoneNumber, inputEmail);
        savedContact.setId(1);
        when(contactRepoMock.save(any(Contact.class))).thenReturn(savedContact);
        when(contactRepoMock.findById(1)).thenReturn(Optional.of(savedContact));

        String res = mainController.doIdentityReconciliation(inputPhoneNumber, inputEmail);
        assertNotNull(res);
    }
}
