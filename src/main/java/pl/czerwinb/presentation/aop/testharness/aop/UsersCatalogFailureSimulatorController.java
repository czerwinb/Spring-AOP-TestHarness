package pl.czerwinb.presentation.aop.testharness.aop;

import org.springframework.jmx.export.annotation.ManagedOperation;

public interface UsersCatalogFailureSimulatorController {

	@ManagedOperation
	void simulateFailure(int count);
}
