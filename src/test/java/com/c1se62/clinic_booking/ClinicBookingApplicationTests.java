package com.c1se62.clinic_booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ClinicBookingApplicationTests {

	@Test
	void contextLoads() {
	}

}
