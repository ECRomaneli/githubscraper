package br.com.ecromaneli.githubscraper.interfaces;

import br.com.ecromaneli.githubscraper.models.File;

import java.util.List;
import java.util.Map;

public interface ScraperInterface {
    public Map<String, List<File>> getFilesGroupedByExtension(String repository);
}
