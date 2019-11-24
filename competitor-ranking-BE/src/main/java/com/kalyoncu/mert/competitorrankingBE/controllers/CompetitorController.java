package com.kalyoncu.mert.competitorrankingBE.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kalyoncu.mert.competitorrankingBE.models.Competitor;
import com.kalyoncu.mert.competitorrankingBE.services.CompetitorService;

@RestController
public class CompetitorController {

	@Autowired
	private CompetitorService competitorService;

	@RequestMapping(method = RequestMethod.POST, value = "/competitors")
	@CrossOrigin(origins = "http://localhost:4200")
	public List<Competitor> addCompetitorList(@RequestBody List<Competitor> competitor) throws IOException {
		return competitorService.addCompetitorList(competitor);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/competitors/{name}")
	@CrossOrigin(origins = "http://localhost:4200")
	public List<Competitor> getCompetitorInformation(@PathVariable("name") String name) throws IOException {
		return competitorService.getCompetitorInformation(name);
	}

}
