package com.nurkiewicz.webflux.demo.feed;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.common.io.CharStreams;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class ArticlesReader {

    /**
     * TODO (3) Return <code>Flux&lt;SyndEntry&gt;</code>
     * Start by replacing {@link #get(URL)} with {@link #getAsync(URL)}.
     */
    public List<SyndEntry> loadFromOneBlog(URL url) throws IOException, FeedException, ParserConfigurationException, SAXException {
        final String feedBody = get(url);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream is = new ByteArrayInputStream(applyAtomNamespaceFix(feedBody).getBytes(UTF_8));
        Document doc = builder.parse(is);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(doc);
        return feed.getEntries();
    }

    private String applyAtomNamespaceFix(String feedBody) {
        return feedBody.replace("https://www.w3.org/2005/Atom", "http://www.w3.org/2005/Atom");
    }

    private String get(URL url) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() == HttpStatus.MOVED_PERMANENTLY.value()) {
            return get(new URL(conn.getHeaderField("Location")));
        }
        try (final InputStreamReader reader = new InputStreamReader(conn.getInputStream(), UTF_8)) {
            return CharStreams.toString(reader);
        }
    }

    /**
     * TODO (2) Load data asynchronously using {@link org.springframework.web.reactive.function.client.WebClient}
     *
     * @see <a href="https://stackoverflow.com/questions/47655789/how-to-make-reactive-webclient-follow-3xx-redirects">How to make reactive webclient follow 3XX-redirects?</a>
     */
    Mono<String> getAsync(URL url) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

}
