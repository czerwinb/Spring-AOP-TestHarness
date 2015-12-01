package pl.czerwinb.presentation.aop.testharness.aop;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "aop-demo:name=UsersCatalogFailureSimulatorController")
public interface UsersCatalogFailureSimulatorController {

	@ManagedOperation
	void simulateFailure(int count);
}
