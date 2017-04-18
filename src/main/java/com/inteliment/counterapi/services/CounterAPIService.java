package com.inteliment.counterapi.services;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inteliment.counterapi.constants.CounterAPIConstants;

/**
 * CounterAPIService is the service class which implement the logics to read the
 * count the number of occurrence of each text in the file.
 * 
 * @author Anu Mohan Das
 * @version 1.0
 * @since 2017-04-16
 *
 */
@Service
public class CounterAPIService {

	/**
	 * initialize logger
	 */
	private static final Logger LOG = Logger.getLogger(CounterAPIService.class
			.getName());

	/**
	 * getWordCount counts the number of occurrences of each text in the input
	 * file. It returns the response in key:value pair where key is the text and
	 * value is its number of occurrence in the text file
	 * 
	 * @param fileName
	 * @return Map<String, Integer>
	 * @throws IOException
	 */
	public Map<String, Integer> getWordCount(final String fileName)
			throws IOException {

		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader bufReader = null;
		if (LOG.isInfoEnabled()) {
			LOG.info("Begin: getWordCount=============> to count total occurrence and write the text to a Map object from input file:"
					+ fileName);
		}
		// TreeMap is used to ignore the case of the text while counting the
		// Occurrence of the text in the file
		final Map<String, Integer> wordMap = new TreeMap<String, Integer>(
				String.CASE_INSENSITIVE_ORDER);
		try {
			fis = new FileInputStream(fileName);
			dis = new DataInputStream(fis);
			bufReader = new BufferedReader(new InputStreamReader(dis));
			String line = null;
			// read each line of the text file
			while ((line = bufReader.readLine()) != null) {
				// replace the dots and commas to get the proper text counts
				// from the file
				line = line.replaceAll(CounterAPIConstants.CHAR_TO_REMOVE,
						CounterAPIConstants.SINGLE_SPACE);
				// split the text with space as token
				final StringTokenizer stringToken = new StringTokenizer(line,
						CounterAPIConstants.SINGLE_SPACE);
				while (stringToken.hasMoreTokens()) {
					final String eachText = stringToken.nextToken();
					// add each text and its count to a map
					if (wordMap.containsKey(eachText)) {
						wordMap.put(eachText, wordMap.get(eachText)
								+ CounterAPIConstants.NUMBER_ONE);
					} else {
						wordMap.put(eachText, CounterAPIConstants.NUMBER_ONE);
					}
				}
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("writen to Map object the text=count " + wordMap);
			}
		} catch (FileNotFoundException e) {
			throw (FileNotFoundException) new FileNotFoundException()
					.initCause(e);
		} catch (IOException e) {
			throw (IOException) new IOException().initCause(e);

		} finally {
			if (bufReader != null) {
				bufReader.close();
			}
		}
		LOG.info("Finish: getWordCount=============> processing");
		return wordMap;
	}

	/**
	 * sortByValue method sorts the wordMap to get the highest count at top
	 * 
	 * @param wordMap
	 * @return List<Entry<String, Integer>>
	 */
	public List<Entry<String, Integer>> sortByValue(
			final Map<String, Integer> wordMap) {

		LOG.info("Begin: sorting the wordMap using method =============> sortByValue");
		final Set<Entry<String, Integer>> set = wordMap.entrySet();
		final List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
				set);
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(final Map.Entry<String, Integer> obj1,
					final Map.Entry<String, Integer> obj2) {
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		return list;
	}

}
