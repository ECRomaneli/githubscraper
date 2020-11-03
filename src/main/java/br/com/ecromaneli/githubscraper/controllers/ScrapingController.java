package br.com.ecromaneli.githubscraper.controllers;

import br.com.ecromaneli.githubscraper.dto.ExtensionMetadataDTO;
import br.com.ecromaneli.githubscraper.interfaces.IScraperController;
import br.com.ecromaneli.githubscraper.models.File;
import br.com.ecromaneli.githubscraper.models.Repository;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("ScrapingController")
public class ScrapingController implements IScraperController {

    @Override
    public Map<String, List<File>> getFilesGroupedByExtension(String repository) throws ClassNotFoundException {
        Repository repo = Repository.parse(repository);
        repo.setRetrieveMetadata(true);

        return repo.getAllFiles().stream().collect(Collectors.groupingBy(File::getExtension));
    }

    @Override
    public List<ExtensionMetadataDTO> getExtensionMetadata(String repository) throws ClassNotFoundException {
        Repository repo = Repository.parse(repository);
        repo.setRetrieveMetadata(true);

        return ExtensionMetadataDTO.from(repo);
    }
}
