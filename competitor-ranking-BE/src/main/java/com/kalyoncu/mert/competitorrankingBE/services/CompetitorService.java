package com.kalyoncu.mert.competitorrankingBE.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalyoncu.mert.competitorrankingBE.models.Competitor;

@Service
public class CompetitorService {

	public String rank = "Rank ";

	@Autowired
	private FileOperatiorService fileOperatiorService;

	public List<Competitor> addCompetitorList(List<Competitor> competitorList) throws IOException {
		List<Competitor> existingList = fileOperatiorService.readFromFile();
		if (!CompetitorUtils.isNullOrEmpty(existingList)) {
			competitorList = updateExistingList(existingList, competitorList);
		}
		competitorList = sortAndRankCompetitorList(competitorList);
		fileOperatiorService.writeToFile(prepareDataForTheStorage(competitorList));

		return competitorList;
	}

	private List<String> prepareDataForTheStorage(List<Competitor> competitorList) {
		return competitorList.stream().map(x -> rank + x.getRank() + "\t" + x.getName() + "\t" + x.getScore())
				.collect(Collectors.toList());
	}

	private List<Competitor> sortAndRankCompetitorList(List<Competitor> competitorList) {
		return rankCompetitor(
				competitorList.stream()
						.sorted(Comparator.comparingInt(Competitor::getScore).reversed()
								.thenComparing(Comparator.comparing(Competitor::getName)))
						.collect(Collectors.toList()));
	}

	private List<Competitor> rankCompetitor(List<Competitor> competitorList) {
		int i = 1;
		for (int j = 0; j < competitorList.size(); j++) {
			if (j == 0) {
				competitorList.get(j).setRank(i);
				continue;
			}
			if (competitorList.get(j).getScore() < competitorList.get(j - 1).getScore()) {
				i++;
			}
			competitorList.get(j).setRank(i);
		}
		return competitorList;
	}

	private List<Competitor> updateExistingList(List<Competitor> existingList, List<Competitor> competitor) {
		Map<String, Integer> combineMap = new HashMap<String, Integer>();
		for (Competitor oldEntry : existingList) {
			combineMap.put(oldEntry.getName(), oldEntry.getScore());
		}
		for (Competitor newEntry : competitor) {
			processNewEntries(combineMap, newEntry);
		}
		return combineMap.entrySet().stream().map(x -> new Competitor(x.getKey(), x.getValue(), 0))
				.collect(Collectors.toList());
	}

	private void processNewEntries(Map<String, Integer> combineMap, Competitor newEntry) {
		if (combineMap.containsKey(newEntry.getName())) {
			combineMap.put(newEntry.getName(), combineMap.get(newEntry.getName()).intValue() + newEntry.getScore());
		} else {
			combineMap.put(newEntry.getName(), newEntry.getScore());
		}
	}

	public List<Competitor> getCompetitorInformation(String name) throws IOException {
		List<Competitor> competitorList = null;
		List<Competitor> existingList = fileOperatiorService.readFromFile();

		if (!CompetitorUtils.isNullOrEmpty(existingList)) {
			competitorList = findCompetitorInformation(name, existingList);
		}
		return competitorList;
	}

	private List<Competitor> findCompetitorInformation(String name, List<Competitor> existingList) {
		return existingList.stream().filter(x -> x.getName().equals(name)).collect(Collectors.toList());
	}

}