package com.kalyoncu.mert.competitorrankingBE.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kalyoncu.mert.competitorrankingBE.models.Competitor;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileOperatorServiceTest {

	@Autowired
	FileOperatiorService fileOperatorService;

	List<String> fileInTheDirectory = null;
	List<String> stubFileContent = null;
	List<Competitor> stubListOfCompetitors = null;

	@Before
	public void setUp() throws Exception {
		fileInTheDirectory = fileOperatorService.copyFileContentBeforeDeletionForTest();
		fileOperatorService.deleteFile();

		Competitor competitorArray[] = { new Competitor("Company_6", 750, 1), new Competitor("Company_2", 500, 2),
				new Competitor("Company_5", 500, 2), new Competitor("Company_3", 350, 3),
				new Competitor("Company_7", 350, 3), new Competitor("Company_1", 100, 4),
				new Competitor("Company_4", 100, 4) };

		stubListOfCompetitors = Arrays.asList(competitorArray).stream().map(x -> (Competitor) x)
				.collect(Collectors.toList());
		stubFileContent = stubListOfCompetitors.stream()
				.map(x -> "rank " + x.getRank() + "\t" + x.getName() + "\t" + x.getScore())
				.collect(Collectors.toList());
	}

	@Test
	public void readFromFileWithNoFileInDirectoryTest() throws Exception {
		assertNull(fileOperatorService.readFromFile());
		fileOperatorService.writeFileBackAfterDeletionForTest(fileInTheDirectory);
	}

	@Test
	public void readFromFileInDirectoryTest() throws Exception {
		fileOperatorService.writeToFile(stubFileContent);

		List<Competitor> actualListOfCompetitors = fileOperatorService.readFromFile();

		assertThat(stubListOfCompetitors.size()).isEqualTo(actualListOfCompetitors.size());
		for (int i = 0; i < stubListOfCompetitors.size() && i < actualListOfCompetitors.size(); i++) {
			assertThat(stubListOfCompetitors.get(i)).isEqualToComparingFieldByField(actualListOfCompetitors.get(i));
		}

		fileOperatorService.deleteFile();
		fileOperatorService.writeFileBackAfterDeletionForTest(fileInTheDirectory);
	}
	
	@Test
	public void checkFileExistsAfterWriteTest() throws Exception {
		final String fileName = "competitorList.txt";
		fileOperatorService.writeToFile(stubFileContent);
		assertTrue(Files.exists(Paths.get(fileName), new LinkOption[] { LinkOption.NOFOLLOW_LINKS }));
		fileOperatorService.deleteFile();
		fileOperatorService.writeFileBackAfterDeletionForTest(fileInTheDirectory);
	}

}
