import io.abhikr2100.ConcurrentTatoebaSentenceExtractor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

public class ConcurrentTatoebaSentenceExtractorTests {
    @Test
    public void shouldReturnJSONListOnExtractionOfOneURL() throws IOException {
        String mockSentence = "Bonjour, Marie!";
        String mockTranslation = "Hello, Marie!";

        String mockJSON = (
            "{&qt;sentence&qt;:&qt;" + mockSentence + "&qt;," +
            "&qt;translation&qt;:&qt;" + mockTranslation + "&qt;}"
        );

        String mockedHTML = (
            "<div id=\"content\"><div><section><md-content>" +
                "<div ng-init=\"vm.init([], " + mockJSON + ")\"></div>" +
            "</md-content></section></div></div>"
        );

        ArrayList<URL> mockedURLs = new ArrayList<>();
        String mockedURL = "http://doesntexist.xyz/search?from=fra&to=en&page=42";
        try {
            mockedURLs.add(new URL(mockedURL));
        } catch (MalformedURLException ignored) {
            throw new RuntimeException("shouldReturnJSONListOnExtractionOfOneURL: couldn't construct URL");
        }

        Document mockedDocument = Jsoup.parse(mockedHTML);
        Connection mockedConnection = Mockito.mock(Connection.class);
        Mockito.when(mockedConnection.get()).thenReturn(mockedDocument);

        Mockito.mockStatic(Jsoup.class);
        Mockito.when(Jsoup.connect(mockedURL)).thenReturn(mockedConnection);

        ArrayList<String> JSONs = ConcurrentTatoebaSentenceExtractor.extractAll(mockedURLs);
        Assertions.assertEquals(1, JSONs.size());

        String json = JSONs.get(0);
        Assertions.assertTrue(json.contains(mockSentence));
        Assertions.assertTrue(json.contains(mockTranslation));
    }
}
