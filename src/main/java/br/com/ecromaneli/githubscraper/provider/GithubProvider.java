package br.com.ecromaneli.githubscraper.provider;

import br.com.ecromaneli.githubscraper.enums.PathType;
import br.com.ecromaneli.githubscraper.factories.PathFactory;
import br.com.ecromaneli.githubscraper.models.Path;
import br.com.ecromaneli.githubscraper.utils.XMLQuery.MLQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GithubProvider extends GitProvider {
    public GithubProvider () {
        this.url = "https://github.com/{{user}}/{{project}}/tree/{{branch}}{{path}}";
        this.rawUrl = "https://raw.githubusercontent.com/{{user}}/{{project}}/{{branch}}{{path}}";
        this.tokenizer = Pattern.compile("com/([^/]*)/([^/]*)(/tree/([^/]*))?");
        this.scraper = (String ml) -> {
            final List<Path> listPath = new ArrayList<>();

            MLQuery.from(ml).find("[role='row']").filter(".Box-row").each((el, _i) -> {
                MLQuery $el = new MLQuery(el);

                String pathTypeStr = $el.find("svg").attr("aria-label");
                if (pathTypeStr.isEmpty()) { return; }
                PathType pathType = PathType.parseLabel(pathTypeStr);

                String pathName = $el.find(".js-navigation-open").text();
                Path path = PathFactory.create(pathType);
                path.setName(pathName);

                listPath.add(path);
            });

            return listPath;
        };
    }
}
