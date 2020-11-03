package com.jtravan.fhirvalidator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FhirValidatorApplicationTests {

	@Test
	@Order(1)
	public void contextLoads() {}

	@Autowired
	WebApplicationContext context;

	@Test
	@Order(2)
	public void generateSwagger() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		mockMvc.perform(MockMvcRequestBuilders.get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
				.andDo((result) -> {
					JsonNode jsonNodeTree = new ObjectMapper().readTree(result.getResponse().getContentAsString());

					((ObjectNode)jsonNodeTree).put("host", "localhost:5000");
					String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);

					// Add harvest cli front matter
					String fullSwaggerString = "---\n" +
							"id: technical-reference\n" +
							"title: Technical Reference\n" +
							"api: hapi-fhir-validator-api\n" +
							jsonAsYaml;

					File technicalReference = new File("docs/technical-reference.yaml");
					if (technicalReference.exists()) {
						technicalReference.delete();
					}

					FileUtils.writeStringToFile(technicalReference, fullSwaggerString, "UTF-8");
				});

	}

}

