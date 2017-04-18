package com.inteliment.counterapi.request;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a wrapper to set the values sent from client while
 * requiring to hit the spring service
 * 
 * @author Anu Mohan Das
 * @version 1.0
 * @since 2017-04-16
 */

public class CounterRequestWrapper {

	private List<String> searchText = new ArrayList<String>();

	/**
	 * method to get the List<String> which is set while coming as an input from
	 * client
	 * 
	 * @return List<String>
	 */
	public List<String> getSearchText() {
		return searchText;
	}

	/**
	 * method to set the List<String> coming as an input from client
	 * 
	 * @param searchText
	 */
	public void setSearchText(final List<String> searchText) {
		this.searchText = searchText;
	}

}
