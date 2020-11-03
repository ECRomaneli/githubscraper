package br.com.ecromaneli.githubscraper.models;

import br.com.ecromaneli.githubscraper.enums.PathType;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter @Setter
public class File extends Path {
    private String extension;
    private Integer linesNumber;
    private Integer bytes;

    public File () {
        setType(PathType.FILE);
    }

    public File (String name) {
        this ();
        setName(name);
    }

    public File (String extension, String name, Integer linesNumber, Integer bytes) {
        this ();
        this.extension = extension;
        this.name = name;
        this.linesNumber = linesNumber;
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
                ", linesNumber=" + linesNumber +
                ", bytes='" + bytes + '\'' +
                '}';
    }
}
