package de.alexa.ws.custom.devops;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;

import de.alexa.ws.AlexaCustomResponse;

public class BuildStatusIntentTest {

	private BuildStatusIntent intent = new BuildStatusIntent();

	@Test
	public void handleStatusFound() throws MalformedURLException, IOException {

		AlexaCustomResponse res = intent.handleIntent("test");
		String txt = res.response.outputSpeech.get("text");
		Assert.assertTrue(txt, txt.startsWith("The latest build with number"));
	}

	@Test
	public void handleStatusNotFound()
			throws MalformedURLException, IOException {

		AlexaCustomResponse res = intent.handleIntent("unknown123");
		String txt = res.response.outputSpeech.get("text");
		Assert.assertTrue(txt, txt.startsWith("Sorry"));
	}
}
