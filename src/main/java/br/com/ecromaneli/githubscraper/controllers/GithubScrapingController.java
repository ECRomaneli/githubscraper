package br.com.ecromaneli.githubscraper.controllers;

import br.com.ecromaneli.githubscraper.interfaces.ScraperInterface;
import br.com.ecromaneli.githubscraper.models.File;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class GithubScrapingController implements ScraperInterface {
    @Override
    public Map<String, List<File>> getFilesGroupedByExtension(String repository) {
        return generateExample();
    }

    private Map<String, List<File>> generateExample() {
        List<File> files = new ArrayList<>();
        files.add(new File("exe", "virus.exe", 300, "300 B"));
        return files.stream().collect(Collectors.groupingBy(File::getExtension));
    }
}
