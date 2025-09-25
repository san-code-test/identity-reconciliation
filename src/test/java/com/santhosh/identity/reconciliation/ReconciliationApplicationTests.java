package com.santhosh.identity.reconciliation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ReconciliationApplicationTests {

	@Autowired
	private MainController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
}
