package com.kalyoncu.mert.competitorrankingBE.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kalyoncu.mert.competitorrankingBE.models.Competitor;

@Service
public class FileOperatiorService {

	public String exception = "IOException: %s%n";
	public String fileName = "competitorList.txt";

	public List<Competitor> readFromFile() throws IOException {
		List<Competitor> existingCompitatorList = null;
		if (Files.exists(Paths.get(fileName), new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
			List<String> content = Files.readAllLines(Paths.get(fileName));

			if (!CompetitorUtils.isNullOrEmpty(content)) {
				existingCompitatorList = content.stream().map(x -> {
					String[] splittedLine = x.split("\t");
					return new Competitor(splittedLine[1], Integer.parseInt(splittedLine[2]),
							Integer.parseInt(splittedLine[0].trim().substring(5)));
				}).collect(Collectors.toList());
			}
		}
		return existingCompitatorList;
	}

	public void writeToFile(List<String> list) {
		Charset utf8 = StandardCharsets.UTF_8;
		try {
			Files.write(Paths.get(fileName), list, utf8);
		} catch (IOException x) {
			System.err.format(exception, x);
		}
	}
	
	public void deleteFile() throws IOException {
			Files.deleteIfExists(Paths.get(fileName));	
	}
	

	public List<String> copyFileContentBeforeDeletionForTest() throws IOException {
		List<String>  fileContent = new ArrayList<String>();
		if (Files.exists(Paths.get(fileName), new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
			 fileContent = Files.readAllLines(Paths.get(fileName));
		}
		return fileContent;
	}
	
	public void writeFileBackAfterDeletionForTest(List<String> list) throws IOException {
		if(!CompetitorUtils.isNullOrEmpty(list)) {
			writeToFile(list);
		}
	}
}
