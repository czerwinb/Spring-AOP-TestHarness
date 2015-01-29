package pl.czerwinb.presentation.aop.testharness.aop;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class UsersCatalogFailureSimulatorAspectTest {

	private static final UUID USER_ID = UUID.randomUUID();

	private UsersCatalogFailureSimulatorAspect aspect = new UsersCatalogFailureSimulatorAspect();

	@Test
	public void exceptionShouldNotBeThrownWhenCounterSaysZero() {
		aspect.simulateFailure(0);

		aspect.simulateFailureIfApplicable(USER_ID);
	}

	@Test
	public void shouldThrowAnExceptionForSpecifiedNumberOfTimesWhenRequested() {
		int counter = 2;
		aspect.simulateFailure(counter);

		while (counter > 0) {
			try {
				aspect.simulateFailureIfApplicable(USER_ID);
				fail("Expected exception not thrown");
			} catch (RuntimeException e) {
				assertThat(e.getMessage(), containsString(USER_ID.toString()));
				counter--;
			}
		}
	}
}