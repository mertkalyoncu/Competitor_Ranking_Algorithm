package com.kalyoncu.mert.competitorrankingBE.services;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalyoncu.mert.competitorrankingBE.models.Competitor;

public final class CompetitorUtils {

	public static <T> boolean isNullOrEmpty(List<T> list) {
		return (list == null || list.size() == 0);
	}
	
	public static byte[] convertObjectToJson(List<Competitor> listOfCompetitors) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(listOfCompetitors);
		return json.getBytes(StandardCharsets.UTF_8);
	}
}
