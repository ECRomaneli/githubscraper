package br.com.ecromaneli.githubscraper.factories;

import br.com.ecromaneli.githubscraper.enums.PathType;
import br.com.ecromaneli.githubscraper.models.Directory;
import br.com.ecromaneli.githubscraper.models.File;
import br.com.ecromaneli.githubscraper.models.Path;

public class PathFactory {
    public static Path create(PathType type) {
        switch (type) {
            case FILE: return new File();
            default: return new Directory();
        }
    }
}
