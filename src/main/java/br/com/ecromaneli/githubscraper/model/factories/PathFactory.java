package br.com.ecromaneli.githubscraper.model.factories;

import br.com.ecromaneli.githubscraper.model.enums.PathType;
import br.com.ecromaneli.githubscraper.model.Directory;
import br.com.ecromaneli.githubscraper.model.File;
import br.com.ecromaneli.githubscraper.model.Path;

public class PathFactory {
    public static Path create(PathType type) {
        switch (type) {
            case FILE: return new File();
            default: return new Directory();
        }
    }
}
