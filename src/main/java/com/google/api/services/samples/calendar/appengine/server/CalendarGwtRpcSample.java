/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.calendar.appengine.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.UriTemplate;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.discovery.Discovery;
import com.google.api.services.discovery.model.JsonSchema;
import com.google.api.services.discovery.model.RestDescription;
import com.google.api.services.discovery.model.RestMethod;
import com.google.gson.Gson;

/**
 * Calendar GWT RPC service implementation.
 * 
 * @author Yaniv Inbar
 */
@SuppressWarnings("serial")
public class CalendarGwtRpcSample extends HttpServlet {

	private static final Logger log = Logger
			.getLogger(CalendarGwtRpcSample.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
		String line=null;
		StringBuffer strb = new StringBuffer();
		while ((line=reader.readLine()) != null) {
			strb.append(line);
		}
		log.info("input=" + strb);
		
		Gson gson = new Gson();
		Input input = gson.fromJson(strb.toString(), Input.class);

		log.info("input=" + input);
		String ret = check2(input);
		log.info("ret=" + ret);
		resp.getWriter().append(ret);
	}

	public String check2(Input input) throws IOException {
		HttpTransport httpTransport = null;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException e) {
			log.log(Level.WARNING, "failed", e);
		}
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		Discovery discovery = new Discovery.Builder(httpTransport, jsonFactory,
				null).build();

		RestDescription api = discovery.apis().getRest("ml", "v1").execute();
		RestMethod method = api.getResources().get("projects").getMethods()
				.get("predict");

		JsonSchema param = new JsonSchema();
		String projectId = "luitest123";
		// You should have already deployed a model and a version.
		// For reference, see
		// https://cloud.google.com/ml-engine/docs/how-tos/deploying-models.
		String modelId = "census";
		String versionId = "v1";
		param.set("name", String.format("projects/%s/models/%s/versions/%s",
				projectId, modelId, versionId));

		GenericUrl url = new GenericUrl(UriTemplate
				.expand(api.getBaseUrl() + method.getPath(), param, true));
		log.info("url=" + url);

		String contentType = "application/json";
		// File requestBodyFile = new File("input.txt");
		// HttpContent content = new FileContent(contentType, requestBodyFile);

		Gson gson = new Gson();
		String i = gson.toJson(input);
		log.info("i=" + i);
		ByteArrayContent content = new ByteArrayContent(contentType,
				i.getBytes());
		// HttpContent content = new InputStreamContent(contentType,
		// getClass().getResourceAsStream("/input.txt"));
		// log.info("len=" + content);
		// content.writeTo(System.out);

		// Credential credential = Utils.getCredential();
		GoogleCredential credential = GoogleCredential.getApplicationDefault();
		List<String> scopes = new ArrayList<String>();
		// scopes.add("https://www.googleapis.com/auth/prediction");
		// scopes.add("https://www.googleapis.com/auth/drive");
		scopes.add("https://www.googleapis.com/auth/cloud-platform");
		// scopes.add("https://www.googleapis.com/auth/cloud-vision");
		// scopes.add("https://www.googleapis.com/auth/cloud.useraccounts");
		scopes.add("https://www.googleapis.com/auth/devstorage.full_control");
		if (credential.createScopedRequired()) {
			log.info("** adding scopes");
			credential = credential.createScoped(scopes);
		}

		log.info("cred=" + credential.getServiceAccountId() + ";"
				+ credential.getServiceAccountUser());
		HttpRequestFactory requestFactory = httpTransport
				.createRequestFactory(credential);
		HttpRequest request = requestFactory
				.buildRequest(method.getHttpMethod(), url, content);

		String response = request.execute().parseAsString();
		log.info(response);

		return response;
	}

	static class Input implements Serializable {
		public List<Item> instances;
	}
	static class Item implements Serializable {
		public Integer age;
		public String workclass, education;
		public Integer education_num;
		public String marital_status, occupation,
				relationship, race, gender;
		public Integer capital_gain, capital_loss,
				hours_per_week;
		public String native_country;
	}
}
