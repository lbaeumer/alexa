package de.alexa.ws.custom.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

import de.alexa.dto.AllBuildsDTO;
import de.alexa.dto.AllJobsDTO;

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
		//Assert.assertEquals(b.lastBuild.number, b.lastFailedBuild.number);
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
	public void getAllJobsFromRss() throws MalformedURLException, IOException, JAXBException, IllegalArgumentException, FeedException {

		SyndFeed b = util.getAllJobsFromRSS();
		System.out.println("updated=" + b.getPublishedDate());
		
		long diff = System.currentTimeMillis() - b.getPublishedDate().getTime();
		System.out.println("seconds = " + diff/1000);
		
		List<SyndEntry> entries = b.getEntries();
		for (SyndEntry s : entries) {
			System.out.println("s=" + s.getTitle() + ";" + s.getPublishedDate());
		}
				
	}
}
