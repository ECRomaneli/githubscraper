package br.com.ecromaneli.githubscraper.provider;

import br.com.ecromaneli.githubscraper.model.Path;
import br.com.ecromaneli.githubscraper.model.Repository;
import br.com.ecromaneli.githubscraper.model.enums.PathType;
import br.com.ecromaneli.githubscraper.model.factories.PathFactory;
import br.com.ecromaneli.githubscraper.util.XMLQuery.MLQuery;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubProvider extends GitProvider {
    public static final String DOMAIN = "github.com";
    private static final Pattern TOKENIZER = Pattern.compile("com/([^/]*)/([^/]*)(/tree/([^/]*))?");

    public GithubProvider () {
        this.url = "https://github.com/{{user}}/{{project}}/tree/{{branch}}{{path}}";
        this.rawUrl = "https://raw.githubusercontent.com/{{user}}/{{project}}/{{branch}}{{path}}";
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

    @Override
    protected Repository toRepository(String rawRepo) throws MalformedURLException {
        Matcher matcher = TOKENIZER.matcher(rawRepo);
        if (!matcher.find()) { throw new MalformedURLException(rawRepo); }

        String user = matcher.group(1);
        String project = matcher.group(2);
        String branch = matcher.group(4);

        return new Repository(this, user, project, branch);
    }

    @Override
    public String toString() {
        return DOMAIN;
    }
}
