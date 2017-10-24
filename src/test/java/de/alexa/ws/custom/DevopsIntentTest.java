package de.alexa.ws.custom;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;

import de.alexa.dto.AllJobsDTO;
import de.alexa.dto.BuildDTO;

public class DevopsIntentTest {

	JenkinsUtil util = new JenkinsUtil();

	@Test
	public void deployTest() throws MalformedURLException, IOException {
		String name = "test";

		int rc = util.startBuild(name,
				"payload=hallo&repositoryUrl=http://myurl");
		Assert.assertEquals(rc, 201);
	}

	@Test
	public void deployTest2() throws MalformedURLException, IOException {
		String name = "test2";

		int rc = util.startBuild(name,
				"payload=hallo&repositoryUrl=http://myurl");
		Assert.assertEquals(rc, 201);
	}

	@Test
	public void deployUnknown() throws MalformedURLException, IOException {
		String name = "unknown_xyz";

		int rc = util.startBuild(name,
				"payload=hallo&repositoryUrl=http://myurl");
		Assert.assertEquals(rc, 404);
	}

	@Test
	public void getLatestBuildSUCCESS() throws MalformedURLException, IOException {
		String name = "test";

		BuildDTO b = util.getLatestBuild(name);
		Assert.assertNotNull(b.id);
		Assert.assertEquals(b.result, "SUCCESS");
	}

	@Test
	public void getLatestBuildFailed() throws MalformedURLException, IOException {
		String name = "test2";

		BuildDTO b = util.getLatestBuild(name);
		Assert.assertNotNull(b.id);
		Assert.assertEquals(b.result, "FAILURE");
	}

	@Test
	public void getLatestBuildNotFound() throws MalformedURLException, IOException {
		String name = "unknown_not_found";

		BuildDTO b = util.getLatestBuild(name);
		Assert.assertNull(b);
	}

	@Test
	public void getAllJobs() throws MalformedURLException, IOException {

		AllJobsDTO b = util.getAllJobs();
		Assert.assertTrue(b.jobs.size() > 0);
	}
}
