package com.c1se62.clinic_booking;

import org.springframework.boot.SpringApplication;

public class TestClinicBookingApplication {

	public static void main(String[] args) {
		SpringApplication.from(ClinicBookingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
