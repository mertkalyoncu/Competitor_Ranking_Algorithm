package com.kalyoncu.mert.competitorrankingBE.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.hamcrest.text.IsEmptyString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.kalyoncu.mert.competitorrankingBE.models.Competitor;
import com.kalyoncu.mert.competitorrankingBE.services.CompetitorUtils;
import com.kalyoncu.mert.competitorrankingBE.services.FileOperatiorService;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CompetitorRankingControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	FileOperatiorService fileOperatorService;

	List<String> fileInTheDirectory = null;

	List<String> stubFileContent = null;

	@Before
	public void setUp() throws Exception {
		fileInTheDirectory = fileOperatorService.copyFileContentBeforeDeletionForTest();
		fileOperatorService.deleteFile();
		Competitor competitorArray[] = { new Competitor("Company_6", 750, 1), new Competitor("Company_2", 500, 2),
				new Competitor("Company_5", 500, 2), new Competitor("Company_3", 350, 3),
				new Competitor("Company_7", 350, 3), new Competitor("Company_1", 100, 4),
				new Competitor("Company_4", 100, 4) };
		List<Competitor> stubListOfCompetitors = Arrays.asList(competitorArray).stream().map(x -> (Competitor) x)
				.collect(Collectors.toList());
		stubFileContent = stubListOfCompetitors.stream()
				.map(x -> "rank " + x.getRank() + "\t" + x.getName() + "\t" + x.getScore())
				.collect(Collectors.toList());
	}

	@Test
	public void getCompetitorInformationWithFileExistingTest() throws Exception {
		fileOperatorService.writeToFile(stubFileContent);
		this.mockMvc.perform(get("/competitors/{name}", "Company_1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].name").value("Company_1"))
				.andExpect(jsonPath("$[0].rank").value(4))
				.andExpect(jsonPath("$[0].score").value(100));
		fileOperatorService.deleteFile();
		fileOperatorService.writeFileBackAfterDeletionForTest(fileInTheDirectory);
	}

	@Test
	public void getCompetitorInformationWithNoFileTest() throws Exception {

		this.mockMvc.perform(get("/competitors/{name}", "Company_1")).andExpect(status().isOk())
				.andExpect(content().string(IsEmptyString.emptyOrNullString()));
		fileOperatorService.writeFileBackAfterDeletionForTest(fileInTheDirectory);
	}

	@Test
	public void addCompetitorListTest() throws Exception {

		Competitor competitorArray[] = { new Competitor("Company_1", 100, 1), new Competitor("Company_2", 200, 1),
				new Competitor("Company_3", 100, 1) };

		List<Competitor> listOfCompetitors = Arrays.asList(competitorArray).stream().map(x -> (Competitor) x)
				.collect(Collectors.toList());

		this.mockMvc
				.perform(post("/competitors").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(CompetitorUtils.convertObjectToJson(listOfCompetitors)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(3)))
				.andExpect(jsonPath("$[0].name").value("Company_2")).andExpect(jsonPath("$[0].rank").value(1))
				.andExpect(jsonPath("$[0].score").value(200)).andExpect(jsonPath("$[1].name").value("Company_1"))
				.andExpect(jsonPath("$[1].rank").value(2)).andExpect(jsonPath("$[1].score").value(100))
				.andExpect(jsonPath("$[2].name").value("Company_3")).andExpect(jsonPath("$[2].rank").value(2))
				.andExpect(jsonPath("$[2].score").value(100)).andReturn();

		fileOperatorService.deleteFile();
		fileOperatorService.writeFileBackAfterDeletionForTest(fileInTheDirectory);

	}

}
