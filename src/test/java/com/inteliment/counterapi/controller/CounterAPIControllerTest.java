package com.inteliment.counterapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.inteliment.counterapi.services.CounterAPIService;

/**
 * Test class for testing CounterAPI service for 1. Searching text occurrence
 * and calculating corresponding count from the given input list of text from a
 * sample input file and return a json response back to client 2. count the
 * highest occurrences of the texts as per the given path variable
 * 
 * @author Anu Mohan Das
 * @version 1.0
 * @since 2017-04-16
 *
 */

@ContextConfiguration(locations = { "file:src/test/resources/applicationContext-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class CounterAPIControllerTest {

	private static final Logger LOG = Logger
			.getLogger(CounterAPIControllerTest.class.getName());

	@Autowired
	private WebApplicationContext ctx;

	private static MockMvc mockMvc;
	
	/**
	 * mocked object
	 */
	@Mock
	private CounterAPIService counterAPIService;

	/**
	 * This method is called before each test case execution
	 */
	@Before
	public void before() {
		LOG.debug("Initializing mocks.....");
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx)
				.dispatchOptions(true).build();
	}

	/**
	 * testGetTextWithHighestCounts tests the highest count of texts fetched
	 * from reading the input file. highest count will be identified based on a
	 * path variable passed through url
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTextWithHighestCounts() throws Exception {
		LOG.debug("Begin: testGetTextWithHighestCounts....");
		this.mockMvc.perform(get("/counter-api/top/20")).andDo(print())
				.andExpect(status().isOk()).andReturn();

	}

	/**
	 * testGetTextCount tests the logic of reading a paragraph from a file and
	 * counting the input texts and send back the number of count of each text
	 * in json format
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTextCount() throws Exception {
		LOG.debug("Begin: testGetTextCount....");
		
		mockMvc.perform(
				post("/counter-api/search")
						.header("Authorization",
								"Basic b3B0dXM6Y2FuZGlkYXRlcw==")
						.contentType(MediaType.APPLICATION_JSON)
						.content(
								"{\"searchText\":[\"Duis\",\"Sed\",\"Donec\",\"Augue\",\"Pellentesque\",\"123\"]}"))
				.andDo(print()).andExpect(status().isOk()).andReturn();

	}
}
