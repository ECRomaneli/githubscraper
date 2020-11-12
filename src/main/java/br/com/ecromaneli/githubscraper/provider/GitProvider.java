package br.com.ecromaneli.githubscraper.provider;

import br.com.ecromaneli.githubscraper.model.Directory;
import br.com.ecromaneli.githubscraper.model.File;
import br.com.ecromaneli.githubscraper.model.Path;
import br.com.ecromaneli.githubscraper.model.Repository;
import br.com.ecromaneli.githubscraper.model.enums.PathType;
import br.com.ecromaneli.githubscraper.util.HttpRequest.HttpRequest;
import br.com.ecromaneli.githubscraper.util.HttpRequest.HttpResponse;
import br.com.ecromaneli.githubscraper.util.HttpRequest.HttpStatus;

import java.net.MalformedURLException;
import java.util.List;
import java.util.function.Function;

public abstract class GitProvider {
    protected String url;
    protected String rawUrl;
    protected Function<String, List<Path>> scraper;

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

    protected abstract Repository toRepository(String rawRepo) throws MalformedURLException;

    @Override
    public abstract String toString();

    public static Repository parse(String rawRepo) throws MalformedURLException {
        return GitProvider.from(rawRepo).toRepository(rawRepo);
    }

    public static GitProvider from(String url) throws MalformedURLException {
        if (url.contains(GithubProvider.DOMAIN)) { return new GithubProvider(); }
        throw new MalformedURLException(url);
    }

    public void fetchChildren(Repository repository, boolean retrieveMetadata) {
        HttpRequest req = new HttpRequest();
        Directory workdir = repository.getWorkdir();
        HttpResponse response = req.get(this.getUrl(repository, workdir));
        if (response.getStatus().equals(HttpStatus.OK)) {

            workdir.setChildren(scraper.apply(response.getData()));
            if (retrieveMetadata) { fetchMetadata(repository); }
        }
    }

    private void fetchMetadata(Repository repository) {
        repository.getWorkdir().getChildren().values().stream()
                .filter(c -> c.getType().equals(PathType.FILE))
                .map(c -> (File) c)
                .forEach(file -> {
                    HttpRequest req = new HttpRequest();
                    HttpResponse response = req.get(this.getRawUrl(repository, file));
                    if (!response.getStatus().equals(HttpStatus.OK)) { return; }

                    byte[] data = response.getBytes();
                    file.setBytes(data.length);
                    file.setLines(getNumberOfLines(response.getData()));
                });
    }

    private static int getNumberOfLines(String str){
        String[] lines = str.split("\r\n|\r|\n", -1);
        return lines.length;
    }
}
