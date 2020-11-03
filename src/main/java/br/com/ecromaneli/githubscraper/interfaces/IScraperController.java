package br.com.ecromaneli.githubscraper.interfaces;

import br.com.ecromaneli.githubscraper.dto.ExtensionMetadataDTO;
import br.com.ecromaneli.githubscraper.models.File;

import java.util.List;
import java.util.Map;

public interface IScraperController {
    Map<String, List<File>> getFilesGroupedByExtension(String url) throws ClassNotFoundException;
    List<ExtensionMetadataDTO> getExtensionMetadata(String repository) throws ClassNotFoundException;
}
