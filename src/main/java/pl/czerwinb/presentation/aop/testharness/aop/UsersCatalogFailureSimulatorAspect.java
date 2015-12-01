package pl.czerwinb.presentation.aop.testharness.aop;


import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

@Aspect
@Component
public class UsersCatalogFailureSimulatorAspect implements UsersCatalogFailureSimulatorController {

	private static Logger LOG = Logger.getLogger(UsersCatalogFailureSimulatorAspect.class);

	private AtomicInteger simulatedFailuresCounter = new AtomicInteger(0);

	@Pointcut(value = "execution(public * *..UsersCatalog.getUserEmail(java.util.UUID)) && args(userId)", argNames = "userId")
	public void getUserEmail(UUID userId) {
	}

	@Before(value = "getUserEmail(userId)", argNames = "userId")
	public void simulateFailureIfApplicable(UUID userId) {
		if (simulatedFailuresCounter.get() > 0) {
			simulatedFailuresCounter.decrementAndGet();
			throw new RuntimeException("Simulated failure for " + userId);
		}
	}

	@Override
	public void simulateFailure(int count) {
		simulatedFailuresCounter.set(count);
		LOG.debug(format("simulateFailure: %d", count));
	}
}
