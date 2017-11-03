package de.alexa.ws.custom.devops;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;

import de.alexa.ws.AlexaCustomResponse;

public class ExecuteJobIntentTest {

	private ExecuteJobIntent intent = new ExecuteJobIntent();

	@Test
	public void handleStatusFound() throws MalformedURLException, IOException {

		AlexaCustomResponse res = intent.handleIntent("test");
		String txt = res.response.outputSpeech.get("text");
		Assert.assertTrue(txt, txt.startsWith("I'm happy to start job test"));
	}

	@Test
	public void handleStatusNotFound()
			throws MalformedURLException, IOException {

		AlexaCustomResponse res = intent.handleIntent("unknown123");
		String txt = res.response.outputSpeech.get("text");
		Assert.assertTrue(txt, txt.startsWith("Sorry"));
	}
}
