//@@author kokonguyen191
package seedu.recipe.logic.commands.util;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.restfb.json.Json;
import com.restfb.json.JsonObject;

/**
 * Handles a query to recipes.wikia.com
 */
public class WikiaQueryHandler implements WikiaQuery {

    public static final String QUERY_URL = "http://recipes.wikia.com/search?query=";
    private static final String API_QUERY_URL = "http://recipes.wikia.com/api/v1/Search/List?query=";

    private String recipeToSearch;
    private URL queryUrl;
    private HttpURLConnection httpUrlConnection;
    private String rawDataString;
    private JsonObject rawDataJson;

    public WikiaQueryHandler(String recipeToSearch) throws AssertionError {
        requireNonNull(recipeToSearch);
        this.recipeToSearch = recipeToSearch;
        loadUrl();
        startHttpConnection();
        getRawData();
        parseData();
    }

    @Override
    public String getRecipeQueryUrl() {
        return QUERY_URL + this.recipeToSearch;
    }

    @Override
    public int getQueryNumberOfResults() {
        return rawDataString == null ? 0 : rawDataJson.get("total").asInt();
    }

    /**
     * Creates a {@code URL} object from given {@code recipeToSearch} string.
     */
    private void loadUrl() {
        requireNonNull(recipeToSearch);
        try {
            queryUrl = new URL(API_QUERY_URL + recipeToSearch);
        } catch (MalformedURLException mue) {
            throw new AssertionError("Invalid query URL. This should not happen.", mue);
        }
    }

    /**
     * Creates a HTTP connection from the {@code URL} object.
     */
    private void startHttpConnection() {
        requireNonNull(queryUrl);
        try {
            httpUrlConnection = (HttpURLConnection) queryUrl.openConnection();
            httpUrlConnection.setRequestMethod("GET");
        } catch (IOException ioe) {
            throw new AssertionError("Something went wrong while the app was "
                    + "trying to create a connection to " + queryUrl.toExternalForm(), ioe);
        }
    }

    /**
     * Reads the HTTP connection and print data to {@code rawDataString}.
     * Adapted from https://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java
     */
    private void getRawData() throws AssertionError {
        requireNonNull(queryUrl);
        requireNonNull(httpUrlConnection);

        StringBuilder result = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();
            rawDataString = result.toString();
        } catch (FileNotFoundException fnfe) {
            rawDataString = null;
        } catch (IOException ie) {
            throw new AssertionError("Something wrong happened while the app "
                    + "was trying to read data from the url " + queryUrl.toExternalForm(), ie);
        }
    }

    /**
     * Gets a {@code JSONObject} from {@code rawDataString}
     */
    private void parseData() {
        if (rawDataString != null) {
            rawDataJson = (JsonObject) Json.parse(rawDataString);
        }
    }
}
