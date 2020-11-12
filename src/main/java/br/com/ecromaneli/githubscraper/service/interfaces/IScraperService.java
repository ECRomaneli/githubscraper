package br.com.ecromaneli.githubscraper.service.interfaces;

import br.com.ecromaneli.githubscraper.model.File;
import br.com.ecromaneli.githubscraper.model.dto.ExtensionMetadataDTO;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface IScraperService {
    Map<String, List<File>> getFilesGroupedByExtension(String url) throws MalformedURLException;
    List<ExtensionMetadataDTO> getExtensionMetadata(String repository) throws MalformedURLException;
    List<String> getCachedRepositories();
}
