package br.com.alura.client;


import com.google.gson.Gson;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class

ClientHttpConfiguration {

    public HttpResponse<String> dispararRequisicaoGet(String uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    //sempre que for receber o parametro tem que por o tipo JsonObject json.
    public HttpResponse<String> dispararRequisicaoPost(String uri, Object object) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();  // ou log de erro adequado
            return null;  // ou tratamento de erro mais refinado
        }
    }
}
