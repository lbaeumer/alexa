package de.alexa.ws.custom.devops;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;

import de.alexa.ws.AlexaCustomResponse;

public class AllJobsIntentTest {

	private AllJobsIntent intent = new AllJobsIntent();

	@Test
	public void handleAllJobs() throws MalformedURLException, IOException {

		AlexaCustomResponse res = intent.handleIntent();
		String txt = res.response.outputSpeech.get("text");
		Assert.assertTrue(txt.startsWith("I found"));
	}
}
