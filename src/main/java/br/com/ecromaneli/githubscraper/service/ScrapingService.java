package br.com.ecromaneli.githubscraper.service;

import br.com.ecromaneli.githubscraper.service.interfaces.IScraperService;
import br.com.ecromaneli.githubscraper.model.File;
import br.com.ecromaneli.githubscraper.model.Repository;
import br.com.ecromaneli.githubscraper.model.dto.ExtensionMetadataDTO;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScrapingService implements IScraperService {

    @Override
    public Map<String, List<File>> getFilesGroupedByExtension(String repository) throws MalformedURLException {
        Repository repo = Repository.parse(repository);
        repo.setRetrieveMetadata(true);

        return repo.getAllFiles().stream().collect(Collectors.groupingBy(File::getExtension));
    }

    @Override
    public List<ExtensionMetadataDTO> getExtensionMetadata(String repository) throws MalformedURLException {
        Repository repo = Repository.parse(repository);
        repo.setRetrieveMetadata(true);

        return ExtensionMetadataDTO.from(repo);
    }
}
