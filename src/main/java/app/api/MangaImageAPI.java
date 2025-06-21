package app.api;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class MangaImageAPI {
    private static final String API_URL = "https://kitsu.io/api/edge/manga?filter[text]=";
    private HttpClient client = HttpClient.newHttpClient();

    public byte[] buscarImagemPorTitulo(String titulo) {
        // TODO: implementar busca na API e download da imagem
        // Exemplo de requisição:
        // HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL + URLEncoder.encode(titulo, StandardCharsets.UTF_8))).build();
        // HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return null;
    }
} 