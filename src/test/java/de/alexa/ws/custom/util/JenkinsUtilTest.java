package de.alexa.ws.custom.util;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import de.alexa.dto.AllBuildsDTO;
import de.alexa.dto.AllJobsDTO;
import de.alexa.jenkins.dto.Feed;

public class JenkinsUtilTest {

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
	public void getLatestBuildSuccess()
			throws MalformedURLException, IOException {
		String name = "test";

		AllBuildsDTO b = util.getAllBuilds(name);
		Assert.assertNotNull(b.name);
		Assert.assertEquals(b.lastBuild.number, b.lastSuccessfulBuild.number);
	}

	@Test
	public void getLatestBuildFailed()
			throws MalformedURLException, IOException {
		String name = "test2";

		AllBuildsDTO b = util.getAllBuilds(name);
		Assert.assertNotNull(b.name);
		Assert.assertEquals(b.lastBuild.number, b.lastFailedBuild.number);
	}

	@Test
	public void getLatestBuildNotFound()
			throws MalformedURLException, IOException {
		String name = "unknown_not_found";

		AllBuildsDTO b = util.getAllBuilds(name);
		Assert.assertNull(b);
	}

	@Test
	public void getAllJobs() throws MalformedURLException, IOException {

		AllJobsDTO b = util.getAllJobs();
		Assert.assertTrue(b.jobs.size() > 0);
	}

	@Test
	public void getAllJobsFromRss() throws MalformedURLException, IOException, JAXBException {

		Feed b = util.getAllJobsFromRSS();
		System.out.println("updated=" + b.getUpdated());
		
		long diff = System.currentTimeMillis() - b.getUpdated().getMillisecond();
		System.out.println("hours = " + diff/1000/60/60);
	}
}
