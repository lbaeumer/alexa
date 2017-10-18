package de.alexa.ws.custom;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class DevopsIntentTest {

	@Test
	public void test() throws MalformedURLException, IOException {
		String name = "test";
		DevopsIntent intent = new DevopsIntent();
		intent.startBuild(new URL("http://webhook:xyz@capture.mobilesol.de:8080/jenkins/job/"
				+ name + "/buildWithParameters?token=1234567890"),
				"payload=hallo&repositoryUrl=http://myurl");

	}

}
