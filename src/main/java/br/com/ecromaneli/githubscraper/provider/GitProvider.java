package br.com.ecromaneli.githubscraper.provider;

import br.com.ecromaneli.githubscraper.models.Path;
import br.com.ecromaneli.githubscraper.models.Repository;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public abstract class GitProvider {
    @Getter protected String name;
    protected String url;
    protected String rawUrl;
    @Getter protected Pattern tokenizer;
    @Getter protected Function<String, List<Path>> scraper;

    public String getUrl(Repository repository, Path path) {
        return setUrlTokens(url, repository, path);
    }

    public String getRawUrl(Repository repository, Path path) {
        return setUrlTokens(rawUrl, repository, path);
    }

    private static String setUrlTokens(String url, Repository repository, Path path) {
        return url  .replace("{{user}}", repository.getUser())
                .replace("{{project}}", repository.getProject())
                .replace("{{branch}}", repository.getBranch())
                .replace("{{path}}", path.getAbsolutePath());
    }

    public static GitProvider parse(String url) throws ClassNotFoundException {
        if (url.contains("github")) { return new GithubProvider(); }
        throw new ClassNotFoundException();
    }
}
