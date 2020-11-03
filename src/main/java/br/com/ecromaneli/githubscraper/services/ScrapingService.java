package br.com.ecromaneli.githubscraper.services;

import br.com.ecromaneli.githubscraper.dto.ExtensionMetadataDTO;
import br.com.ecromaneli.githubscraper.interfaces.IScraperController;
import br.com.ecromaneli.githubscraper.models.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ScrapingService {

    @Autowired
    IScraperController controller;

    @GetMapping("/getFilesGroupedByExtension")
    public Map<String, List<File>> getFilesGroupedByExtension(@RequestParam String repository) throws ClassNotFoundException {
        return controller.getFilesGroupedByExtension(repository);
    }

    @GetMapping("/getExtensionMetadata")
    public List<ExtensionMetadataDTO> getExtensionMetadata(@RequestParam String repository) throws ClassNotFoundException {
        return controller.getExtensionMetadata(repository);
    }
}
