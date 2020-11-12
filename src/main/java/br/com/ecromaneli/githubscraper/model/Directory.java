package br.com.ecromaneli.githubscraper.model;

import br.com.ecromaneli.githubscraper.model.enums.PathType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Directory extends Path {
    public static final Directory EMPTY_DIR = new Directory();

    @JsonIgnore
    private Map<String, Path> children = new HashMap<>();

    public Path getChild(String name) {
        return children.get(name);
    }

    public void setChild(Path child) {
        this.children.put(child.getName(), child);
        child.setParent(this);
    }

    public void setChildren(List<Path> children) {
        children.stream().forEach(c -> setChild(c));
    }

    public Directory () {
        setType(PathType.DIRECTORY);
    }

    public Directory (String name) {
        this ();
        setName(name);
    }

    @Override
    public String toString() {
        return "Directory{" +
                "name='" + name + '\'' +
                '}';
    }
}
