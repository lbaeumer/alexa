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
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

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

		super.service(arg0, arg1);
	}

}
