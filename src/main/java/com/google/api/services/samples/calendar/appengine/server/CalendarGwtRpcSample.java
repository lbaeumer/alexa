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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.Prediction.Trainedmodels;
import com.google.api.services.prediction.Prediction.Trainedmodels.Predict;
import com.google.api.services.prediction.model.Input;
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
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		log.info("serve calendar");
		check2();
		super.service(arg0, arg1);
	}
	
	public void check1() throws IOException {
		Prediction prediction = Utils.loadPredictionClient();
		log.info("prediction=" + prediction);

		Trainedmodels tm = prediction.trainedmodels();
		log.info("tm=" + tm);

		Prediction.Trainedmodels.List l;
		l = tm.list("census");
		log.info("tm list=" + l);
		log.info("tm list=" + l.getProject() + ";" + l.getKey());

		l = tm.list("v1");
		log.info("tm list=" + l);
		log.info("tm list=" + l.getProject() + ";" + l.getKey());

		l = tm.list("luitest123");
		log.info("tm list=" + l);
		log.info("tm list=" + l.getProject() + ";" + l.getKey());

		Input input = new Input();
		input.put("age", 25);
		input.put("workclass", "Private");
		input.put("education", "11th");
		input.put("education_num", 7);
		input.put("marital_status", "Never-married");
		input.put("occupation", "Machine-op-inspct");
		input.put("relationship", "Own-child");
		input.put("race", "Black");
		input.put("gender", "Male");
		input.put("capital_gain", 0);
		input.put("capital_loss", 0);
		input.put("hours_per_week", 40);
		input.put("native_country", "United-States");
		Predict predict = tm.predict("luitest123", "census", input);
		log.info("tm predict=" + predict);
		log.info("tm predict=" + predict.getId());
		log.info("tm predict=" + predict.getLastStatusMessage());
		log.info("tm predict=" + predict.getFields());
	}

	public void check2() throws IOException {
	    HttpTransport httpTransport = null;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException e) {
			log.log(Level.WARNING, "failed", e);
		}
	    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
	    Discovery discovery = new Discovery.Builder(httpTransport, jsonFactory, null).build();

	    RestDescription api = discovery.apis().getRest("ml", "v1").execute();
	    RestMethod method = api.getResources().get("projects").getMethods().get("predict");

	    JsonSchema param = new JsonSchema();
	    String projectId = "luitest123";
	    // You should have already deployed a model and a version.
	    // For reference, see https://cloud.google.com/ml-engine/docs/how-tos/deploying-models.
	    String modelId = "census";
	    String versionId = "v1";
	    param.set(
	        "name", String.format("projects/%s/models/%s/versions/%s", projectId, modelId, versionId));

	    GenericUrl url =
	        new GenericUrl(UriTemplate.expand(api.getBaseUrl() + method.getPath(), param, true));
	    log.info("url=" + url);

		String contentType = "application/json";
//		File requestBodyFile = new File("input.txt");
//		HttpContent content = new FileContent(contentType, requestBodyFile);
		HttpContent content = new InputStreamContent(contentType, 
				getClass().getResourceAsStream("/input.txt"));
		//log.info("len=" + content);
		//content.writeTo(System.out);

		// Credential credential = Utils.getCredential();
		GoogleCredential credential = GoogleCredential.getApplicationDefault();
		List<String> scopes = new ArrayList<String>();
		scopes.add("https://www.googleapis.com/auth/prediction");
		scopes.add("https://www.googleapis.com/auth/drive");
		scopes.add("https://www.googleapis.com/auth/cloud-platform");
		scopes.add("https://www.googleapis.com/auth/cloud-vision");
		scopes.add("https://www.googleapis.com/auth/cloud.useraccounts");
		scopes.add("https://www.googleapis.com/auth/devstorage.full_control");
		if (credential.createScopedRequired()) {
			log.info("** adding scopes");
			credential = credential.createScoped(scopes);
		}

		
		log.info("cred=" + credential.getServiceAccountId()
		+ ";" + credential.getServiceAccountUser());
		HttpRequestFactory requestFactory = httpTransport
				.createRequestFactory(credential);
		HttpRequest request = requestFactory
				.buildRequest(method.getHttpMethod(), url, content);

	    String response = request.execute().parseAsString();
	    log.info(response);
	  }
}
