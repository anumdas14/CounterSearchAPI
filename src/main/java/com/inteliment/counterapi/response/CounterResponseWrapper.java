package com.inteliment.counterapi.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class provides a wrapper to set the values sent back to client while
 * returning response from spring service
 * 
 * @author Anu Mohan Das
 * @version 1.0
 * @since 2017-04-16
 */
public class CounterResponseWrapper {

	private List<Map<String, Integer>> counts = new ArrayList<Map<String, Integer>>();

	/**
	 * This gets the highest counts 0f text read from a file
	 * 
	 * @return List<Map<String, Integer>>
	 */
	public List<Map<String, Integer>> getCounts() {
		return counts;
	}

	/**
	 * This sets the highest counts 0f text read from a file
	 * 
	 */
	public void setCounts(final List<Map<String, Integer>> counts) {
		this.counts = counts;
	}

}
