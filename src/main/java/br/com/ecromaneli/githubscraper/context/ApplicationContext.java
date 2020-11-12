package br.com.ecromaneli.githubscraper.context;

import br.com.ecromaneli.githubscraper.model.Repository;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private static final ApplicationContext instance = new ApplicationContext();
    @Getter
    private final Map<String, Repository> cachedRepositories = Collections.synchronizedMap(new HashMap<>());

    private ApplicationContext() {}

    public static ApplicationContext getInstance() {
        return instance;
    }

    public boolean hasRepository(Repository repository) {
        return cachedRepositories.containsKey(repository.toString());
    }

    public Repository getRepository(Repository repository) {
        return cachedRepositories.get(repository.toString());
    }

    public void setRepository(Repository repository) {
        cachedRepositories.put(repository.toString(), repository);
    }
}
