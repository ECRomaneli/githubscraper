package br.com.ecromaneli.githubscraper.service.interfaces;

import br.com.ecromaneli.githubscraper.model.dto.ExtensionMetadataDTO;
import br.com.ecromaneli.githubscraper.model.File;

import java.util.List;
import java.util.Map;

public interface IScraperService {
    Map<String, List<File>> getFilesGroupedByExtension(String url) throws ClassNotFoundException;
    List<ExtensionMetadataDTO> getExtensionMetadata(String repository) throws ClassNotFoundException;
}
