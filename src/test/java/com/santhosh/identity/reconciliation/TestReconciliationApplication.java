package com.santhosh.identity.reconciliation;

import org.springframework.boot.SpringApplication;

public class TestReconciliationApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReconciliationApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
