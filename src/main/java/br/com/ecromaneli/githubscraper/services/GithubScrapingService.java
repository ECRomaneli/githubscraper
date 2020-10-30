package br.com.ecromaneli.githubscraper.services;

import br.com.ecromaneli.githubscraper.controllers.GithubScrapingController;
import br.com.ecromaneli.githubscraper.models.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GithubScrapingService {

    @Autowired
    GithubScrapingController controller;

    @GetMapping("/getFilesGroupedByExtension")
    public Map<String, List<File>> getFilesGroupedByExtension(@RequestParam String repository) {
        return controller.getFilesGroupedByExtension(repository);
    }
}
