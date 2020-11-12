package br.com.ecromaneli.githubscraper.model;

import br.com.ecromaneli.githubscraper.model.enums.PathType;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter @Setter
public class File extends Path {
    private String extension;
    private Integer lines;
    private Integer bytes;

    public File () {
        setType(PathType.FILE);
    }

    public File (String name) {
        this ();
        setName(name);
    }

    public File (String extension, String name, Integer lines, Integer bytes) {
        this ();
        this.extension = extension;
        this.name = name;
        this.lines = lines;
        this.bytes = bytes;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        setExtension();
    }

    private void setExtension() {
        if (name == null) { return; }
        String[] dotSeparatedName = name.split("\\.");
        if (dotSeparatedName.length > 1) {
            extension = dotSeparatedName[dotSeparatedName.length - 1].toLowerCase(Locale.US);
        } else {
            extension = "";
        }
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", extension='" + extension + '\'' +
                ", linesNumber=" + lines +
                ", bytes='" + bytes + '\'' +
                '}';
    }
}
