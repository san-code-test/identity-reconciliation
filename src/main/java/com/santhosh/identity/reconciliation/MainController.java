package com.santhosh.identity.reconciliation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class MainController {
	@Autowired
	private IdentityReconciliationService identityReconciliationService;

	@PostMapping(path = "/identity", consumes = "application/json", produces = "application/json")
	public @ResponseBody String doIdentityReconciliation(@RequestBody IdentityRequest request) {
		if (request.getPhoneNumber() == null && request.getEmail() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
				"Both phoneNumber and email must not be empty.");
		}

		return identityReconciliationService.reconcileIdentity(
            request.getPhoneNumber(),
            request.getEmail()
        );
	}
}
