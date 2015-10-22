package pl.czerwinb.presentation.aop.testharness;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.czerwinb.presentation.aop.testharness.domain.Request;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration({
		"classpath:spring/integration-test-context.xml"
})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class RequestProcessorIntegrationTest {

	private static final ClassPathResource REQUESTS_INPUT_PATH_RESOURCE = new ClassPathResource("/tmp/requests-input");
	private static final int FIVE_SECONDS = 5000;

	@Autowired private PollableChannel enrichedRequestsWireTapChannel;

	@Test
	public void shouldProcessAndLogRequest() throws IOException {
		String randomUserId = givenRandomUserId();

		whenProcessingIsTriggeredByNewRequestFile(randomUserId);

		Request request = thenRequestIsReceived();
		assertThat(request.getUserId().toString(), is(equalTo(randomUserId)));
	}

	private void whenProcessingIsTriggeredByNewRequestFile(String userId) throws IOException {
		File temporaryFile = createTemporaryFile(userId);
		writeRandomIdIntoFile(temporaryFile, userId);
	}

	private Request thenRequestIsReceived() {
		Message<Request> message = (Message<Request>) enrichedRequestsWireTapChannel.receive(FIVE_SECONDS);
		return message.getPayload();
	}

	private static String givenRandomUserId() {
		return randomUUID().toString();
	}

	private static File createTemporaryFile(String fileName) throws IOException {
		File inputFile = File.createTempFile(fileName, ".req", REQUESTS_INPUT_PATH_RESOURCE.getFile());
		inputFile.deleteOnExit();
		return inputFile;
	}

	private static void writeRandomIdIntoFile(File file, String userId) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(userId);
		fileWriter.close();
	}
}