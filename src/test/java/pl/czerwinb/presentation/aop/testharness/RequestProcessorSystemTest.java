package pl.czerwinb.presentation.aop.testharness;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.czerwinb.presentation.aop.testharness.aop.UsersCatalogFailureSimulatorController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@ContextConfiguration({
		"classpath:spring/system-tests-context.xml"
})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class RequestProcessorSystemTest {

	private static final int FIVE_SECONDS = 5000;

	@Autowired private PollableChannel erroredMessagesChannel;
	@Autowired private UsersCatalogFailureSimulatorController usersCatalogFailureSimulatorControllerProxy;

	@Test
	public void messageShouldBePutOnErrorsQueueWhenUserCatalogIsNotAccessible() throws IOException {
		givenUserCatalogWillFail();
		String randomUserId = givenRandomUserId();

		whenProcessingIsTriggeredByNewRequestFile(randomUserId);

		Exception exception = thenProcessingExceptionIsPutOnErrorsQueue();
		assertThat(exception.getMessage(), containsString("Simulated failure"));
		assertThat(exception.getMessage(), containsString(randomUserId));
	}

	private void givenUserCatalogWillFail() {
		usersCatalogFailureSimulatorControllerProxy.simulateFailure(1);
	}

	private void whenProcessingIsTriggeredByNewRequestFile(String userId) throws IOException {
		File tempFile = createTemporaryFile(userId);
		writeUserIdIntoFile(tempFile, userId);
	}

	private static String givenRandomUserId() {
		return randomUUID().toString();
	}

	private static File createTemporaryFile(String fileName) throws IOException {
		File inputFile = File.createTempFile(fileName, ".req", new File("/tmp/requests-input"));
		inputFile.deleteOnExit();
		return inputFile;
	}

	private static void writeUserIdIntoFile(File file, String userId) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(userId);
		fileWriter.close();
	}

	private Exception thenProcessingExceptionIsPutOnErrorsQueue() {
		Message<Exception> message = (Message<Exception>) erroredMessagesChannel.receive(FIVE_SECONDS);
		assertThat(message, is(notNullValue()));
		return message.getPayload();
	}
}