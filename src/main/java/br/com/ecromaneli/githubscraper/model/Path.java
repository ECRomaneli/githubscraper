package br.com.ecromaneli.githubscraper.model;

import br.com.ecromaneli.githubscraper.model.enums.PathType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public abstract class Path {
    protected String name;
    protected PathType type;
    @JsonIgnore protected Path parent;

    public String getAbsolutePath() {
        if (parent == null) { return ""; }
        return parent.getAbsolutePath() + "/" + name;
    }
}
