package br.com.ecromaneli.githubscraper.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PathType {
    DIRECTORY, FILE;

    public static PathType parseLabel(String label) {
        if (label.equalsIgnoreCase(DIRECTORY.name())) { return DIRECTORY; }
        return FILE;
    }
}
