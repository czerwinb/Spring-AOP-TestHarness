package pl.czerwinb.presentation.aop.testharness;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class Bootstrap {

	private static final String[] ROOT_CONTEXTS = new String[]{"classpath:spring/application-root-context.xml"};

	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(ROOT_CONTEXTS, false);
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		environment.setActiveProfiles("test");
		applicationContext.refresh();
	}
}
