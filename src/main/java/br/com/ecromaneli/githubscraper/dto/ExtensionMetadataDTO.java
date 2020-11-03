package br.com.ecromaneli.githubscraper.dto;

import br.com.ecromaneli.githubscraper.models.File;
import br.com.ecromaneli.githubscraper.models.Repository;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ExtensionMetadataDTO implements Serializable {
    private String extension;
    private long lines;
    private long bytes;

    private ExtensionMetadataDTO (String extension, List<File> files) {
        this.extension = extension;
        lines = 0;
        bytes = 0;
        for (File file : files) {
            if (file.getLinesNumber() != null) { lines += file.getLinesNumber(); }
            if (file.getBytes() != null) { bytes += file.getBytes(); }
        }
    }

    public static List<ExtensionMetadataDTO> from(Repository repo) {
        Map<String, List<File>> filesByExtension = repo.getAllFiles().stream()
                .collect(Collectors.groupingBy(File::getExtension));

        final List<ExtensionMetadataDTO> dtos = new ArrayList<>();
        filesByExtension.forEach((ext, files) -> dtos.add(new ExtensionMetadataDTO(ext, files)));
        return dtos;
    }
}
