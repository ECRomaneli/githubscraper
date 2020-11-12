package br.com.ecromaneli.githubscraper.model;

import br.com.ecromaneli.githubscraper.context.ApplicationContext;
import br.com.ecromaneli.githubscraper.model.enums.PathType;
import br.com.ecromaneli.githubscraper.provider.GitProvider;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
public class Repository {
    private GitProvider provider;
    private String user;
    private String project;
    private String branch;
    private Directory workdir;
    private boolean retrieveMetadata = false;

    public Repository (GitProvider provider, String user, String project, String branch, Directory directory) {
        this.provider = provider;
        this.user = user;
        this.project = project;
        this.branch = branch != null ? branch : "master";
        this.workdir = directory != null ? directory : new Directory("");
    }

    public Repository (GitProvider provider, String user, String project, String branch) {
        this (provider, user, project, branch, null);
    }

    public Repository (GitProvider provider, String user, String project) {
        this (provider, user, project, null, null);
    }

    public Repository navigateTo(String dest) {
        if (!existsChildren()) { update(); }

        Path destPath = workdir.getChild(dest);
        workdir = (Directory) destPath;

        return this;
    }

    public Repository navigateTo(Path path) {
        navigateTo(path.getName());
        return this;
    }

    public Path get(String pathName) {
        if (!existsChildren()) { update(); }

        return workdir.getChild(pathName);
    }

    public Map<String, Path> getAll() {
        if (!existsChildren()) { update(); }
        return workdir.getChildren();
    }

    public List<File> getAllFiles() {
        List<File> files = new ArrayList<>();
        getAllFiles(files);
        return files;
    }

    private void getAllFiles(final List<File> files) {
        if (!existsChildren()) { update(); }

        Map<PathType, List<Path>> pathByType = getWorkdir().getChildren().values()
                .stream().collect(Collectors.groupingBy(Path::getType));

        if (pathByType.containsKey(PathType.FILE)) {
            List<File> newFiles = pathByType.get(PathType.FILE).stream()
                    .map(f -> (File) f).collect(Collectors.toList());
            synchronized (files) { files.addAll(newFiles); }
        }

        if (pathByType.containsKey(PathType.DIRECTORY)) {
            pathByType.get(PathType.DIRECTORY).forEach(d -> clone((Directory) d).getAllFiles(files));
        }
    }

    private boolean existsChildren() {
        return !workdir.getChildren().isEmpty();
    }

    public void update() {
        provider.fetchChildren(this, isRetrieveMetadata());
    }

    protected Repository clone(Directory newWorkdir) {
        Repository repo = new Repository(provider, user, project, branch, newWorkdir);
        repo.setRetrieveMetadata(this.isRetrieveMetadata());
        return repo;
    }

    public static Repository parse(String rawRepo) throws MalformedURLException {
        Repository repo = GitProvider.parse(rawRepo);

        ApplicationContext context = ApplicationContext.getInstance();
        if (context.hasRepository(repo)) {
            repo = context.getRepository(repo);
        } else {
            context.setRepository(repo);
        }

        return repo;
    }

    @Override
    public String toString() {
        return provider.getUrl(this, Directory.EMPTY_DIR);
    }
}
