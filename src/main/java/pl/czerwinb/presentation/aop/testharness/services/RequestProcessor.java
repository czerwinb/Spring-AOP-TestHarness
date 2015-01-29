package pl.czerwinb.presentation.aop.testharness.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import pl.czerwinb.presentation.aop.testharness.domain.Request;

import java.util.UUID;

import static pl.czerwinb.presentation.aop.testharness.domain.Request.NamedProperty.USER_EMAIL;

@Service
public class RequestProcessor {

	private static Logger LOG = Logger.getLogger(RequestProcessor.class);

	@Autowired private UsersCatalog usersCatalog;

	@ServiceActivator
	public Request process(Request request) {
		LOG.debug("Processing request: " + request.getUserId());
		enrichWithUserEmail(request);
		return request;
	}

	private void enrichWithUserEmail(Request request) {
		String userEmail = getUserEmail(request.getUserId());
		request.setProperty(USER_EMAIL, userEmail);
	}

	private String getUserEmail(UUID userId) {
		return usersCatalog.getUserEmail(userId);
	}
}
