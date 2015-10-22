package pl.czerwinb.presentation.aop.testharness.services;

import java.util.UUID;

public class DummyUsersCatalog implements UsersCatalog {

	@Override
	public String getUserEmail(UUID userId) {
		return userId + "@example.com";
	}
}
