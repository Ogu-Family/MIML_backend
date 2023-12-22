package com.miml.c2k.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miml.c2k.domain.movie.Movie;
import com.miml.c2k.domain.movie.repository.MovieRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KobisOpenApiUtil {

    private final MovieRepository movieRepository;
    private final CloseableHttpClient httpClient;
    @Value("${spring.kobis.url.base}")
    private String baseUrl;
    @Value("${spring.kobis.url.boxoffice-top10}")
    private String boxOfficeTop10Url;
    @Value("${spring.kobis.url.movie-info}")
    private String movieInfoUrl;
    @Value("${spring.kobis.key}")
    private String key;

    public KobisOpenApiUtil(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        httpClient = HttpClientBuilder.create().build();
    }

    @Transactional
    public void saveBoxOfficeTop10ByTargetDate(String targetDate) {
        String boxOfficeTop10BaseUrl = baseUrl + boxOfficeTop10Url;
        try {
            // HTTP GET 요청 생성
            URI requestBoxOfficeTop10Uri = new URIBuilder(boxOfficeTop10BaseUrl)
                    .addParameter("key", key)
                    .addParameter("targetDt", targetDate)
                    .addParameter("weekGb", String.valueOf(0))
                    .build();

            // 요청 전송 및 응답 수신
            JsonNode rootNode = executeHttpRequestAndGetJsonResponse(requestBoxOfficeTop10Uri);

            // 데이터 필터링
            JsonNode weeklyBoxOfficeList = rootNode.path("boxOfficeResult")
                    .path("weeklyBoxOfficeList");
            for (JsonNode movieNode : weeklyBoxOfficeList) {
                Long audienceCount = movieNode.path("audiCnt").asLong();
                String code = movieNode.path("movieCd").asText();

                Movie movie = getMovieInfoByMovieCodeAndSave(code);
                movie.updateAudienceCount(audienceCount);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Movie getMovieInfoByMovieCodeAndSave(String movieCode) {
        String movieInfoBaseUrl = baseUrl + movieInfoUrl;
        Optional<Movie> movie = movieRepository.findByCode(Long.valueOf(movieCode));
        if (movie.isPresent()) {
            return movie.get();
        } else {
            try {
                // HTTP GET 요청 생성
                URI requestMovieInfoUri = new URIBuilder(movieInfoBaseUrl)
                        .addParameter("key", key)
                        .addParameter("movieCd", movieCode)
                        .build();

                // 요청 전송 및 응답 수신
                JsonNode rootNode = executeHttpRequestAndGetJsonResponse(requestMovieInfoUri);

                // 데이터 필터링
                Long code;
                String title, genre, director, nation;

                JsonNode movieInfoJsonNode = rootNode.path("movieInfoResult").path("movieInfo");
                code = movieInfoJsonNode.path("movieCd").asLong();
                title = movieInfoJsonNode.path("movieNm").asText();
                JsonNode directorsNode = movieInfoJsonNode.path("directors");
                director =
                        directorsNode.size() >= 1 ? directorsNode.get(0).path("peopleNm").asText()
                                : null;
                JsonNode genresNode = movieInfoJsonNode.path("genres");
                genre = genresNode.size() >= 1 ? genresNode.get(0).path("genreNm").asText() : null;
                JsonNode nationsNode = movieInfoJsonNode.path("nations");
                nation = nationsNode.size() >= 1 ? nationsNode.get(0).path("nationNm").asText()
                        : null;
                // TODO: 포스터 별도 크롤링 필요

                return movieRepository.save(new Movie(title, director, genre, nation, code));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        throw new RuntimeException();
    }

    private JsonNode executeHttpRequestAndGetJsonResponse(URI uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(result.toString());

        return rootNode;
    }
}