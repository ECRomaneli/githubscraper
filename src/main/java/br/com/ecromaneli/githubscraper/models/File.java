package br.com.ecromaneli.githubscraper.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class File {
    private String extension;
    private String name;
    private Integer linesNumber;
    private String bytes;
}
