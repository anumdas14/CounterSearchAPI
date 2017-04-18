package com.inteliment.counterapi.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inteliment.counterapi.constants.CounterAPIConstants;
import com.inteliment.counterapi.request.CounterRequestWrapper;
import com.inteliment.counterapi.response.CounterResponseWrapper;
import com.inteliment.counterapi.services.CounterAPIService;



/**
 * The CounterAPIController class implements logic for processing the request
 * received from client. It does the following 1. search given list of text from
 * a sample paragraph and return a json response back to client 2. count the
 * highest occurrences of the texts as per the given path variable
 * 
 * @author Anu Mohan Das
 * @version 1.0
 * @since 2017-04-16
 */

@RestController
public class CounterAPIController {

	private static final Logger LOG = Logger
			.getLogger(CounterAPIController.class.getName());
	private final List<Map<String, Integer>> finalListOfText = new ArrayList<Map<String, Integer>>();
	private final CounterResponseWrapper responseWrap = new CounterResponseWrapper();

	@Autowired
	private CounterAPIService counterAPIService;

	/**
	 * getTextWithHighestCounts count the highest occurrences of the texts as
	 * per the given path variable
	 * 
	 * @param count
	 * @return File
	 * @throws IOException
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/counter-api/top/{count}", method = RequestMethod.GET)
	public File getTextWithHighestCounts(
			@PathVariable("count") final Integer count) throws IOException {
		PrintWriter printWriter = null;

		// open response csv file in write mode
		try {
			printWriter = new PrintWriter(new FileWriter(
					CounterAPIConstants.RESPONSE_CSV_FILE), true);
			if (LOG.isInfoEnabled()) {
				LOG.info("Begin: getTextWithHighestCounts  =============> with input count as "
						+ count);
			}
			// get the count of the texts in the paragraph
			final Map<String, Integer> wordMap = counterAPIService
					.getWordCount(CounterAPIConstants.INPUT_FILE);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Text file has been searched. TEXT=counts:" + wordMap);
			}
			// sort it in descending order
			final List<Entry<String, Integer>> list = counterAPIService
					.sortByValue(wordMap);
			// iterate and write the top most in the response csv file
			for (int i = 0; i <= count; i++) {
				for (final Map.Entry<String, Integer> entry : list) {
					final StringBuilder buildTextCount = new StringBuilder();
					buildTextCount.append(entry.getKey())
							.append(CounterAPIConstants.PIPE)
							.append(entry.getValue())
							.append(CounterAPIConstants.NEW_LINE);

					// print each record in the response csv file
					printWriter.print(buildTextCount.toString());
					printWriter.flush();
					i++;
					// if the count is more than the input value come out of the
					// iteration and write process
					if (i > count) {
						break;
					}
				}
			}
			LOG.info("Finish: getTextWithHighestCounts  =============> processing ");
		} finally {
			printWriter.close();
		}

		return new File(CounterAPIConstants.RESPONSE_CSV_FILE);

	}

	/**
	 * getTextCount reads a paragraph from a file and counts the input texts and
	 * send back the number of count of each text in json format
	 * 
	 * @param inputData
	 * @return CounterResponseWrapper
	 * @throws IOException
	 */
	@RequestMapping(value = "/counter-api/search", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@Secured("ROLE_ADMIN")
	public CounterResponseWrapper getTextCount(
			@RequestBody final CounterRequestWrapper reqWrapper) {
		Map<String, Integer> wordMap;
		try {
			wordMap = counterAPIService
					.getWordCount(CounterAPIConstants.INPUT_FILE);

			Map<String, Integer> finalMap = null;

			LOG.info("Begin: processing ===============> getTextCount");
			for (final Map.Entry<String, Integer> entry : wordMap.entrySet()) {
				for (final String in : reqWrapper.getSearchText()) {
					// search each text and write it to the final list
					if (entry.getKey().equalsIgnoreCase(in)) {
						finalMap = new HashMap<String, Integer>();
						finalMap.put(in, entry.getValue());
						finalListOfText.add(finalMap);
						if (LOG.isDebugEnabled()) {
							LOG.debug("Writing the final data to the list finalListOfText"
									+ finalListOfText);
						}
					}
				}
			}
			// check for if key do not present in Map then count is 0
			for (final String searchKey : reqWrapper.getSearchText()) {
				if (!wordMap.containsKey(searchKey)) {
					finalMap = new HashMap<String, Integer>();
					finalMap.put(searchKey, CounterAPIConstants.ZERO);
					finalListOfText.add(finalMap);
					if (LOG.isDebugEnabled()) {
						LOG.debug("Writing the count 0 also to the list finalListOfText"
								+ finalListOfText);
					}
				}
			}

			// set the response to the ResponseWrapper object
			responseWrap.setCounts(finalListOfText);
			LOG.info("Finish: processing ===============> getTextCount");
		} catch (IOException e) {
			LOG.error("An Exception occured while writing text counts", e);
		}
		return responseWrap;
	}

}