package pl.czerwinb.presentation.aop.testharness.aop;

import org.springframework.jmx.export.annotation.ManagedOperation;

//@ManagedResource
public interface UsersCatalogFailureSimulatorController {

	@ManagedOperation
	void simulateFailure(int count);
}
