package br.com.ecromaneli.githubscraper.controller;

import br.com.ecromaneli.githubscraper.model.File;
import br.com.ecromaneli.githubscraper.model.dto.ExtensionMetadataDTO;
import br.com.ecromaneli.githubscraper.service.interfaces.IScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@RestController
public class ScrapingController {

    @Autowired
    IScraperService service;

    @GetMapping("/getFilesGroupedByExtension")
    public Map<String, List<File>> getFilesGroupedByExtension(@RequestParam String repository) throws MalformedURLException {
        return service.getFilesGroupedByExtension(repository);
    }

    @GetMapping("/getExtensionMetadata")
    public List<ExtensionMetadataDTO> getExtensionMetadata(@RequestParam String repository) throws MalformedURLException {
        return service.getExtensionMetadata(repository);
    }

    @GetMapping("/getCachedRepositories")
    public List<String> getCachedRepositories() {
        return service.getCachedRepositories();
    }
}
