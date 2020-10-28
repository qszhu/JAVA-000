import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;

public class MyHttpClient {

    public static void main(String[] args) throws Exception {
        String uri = args[0];
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();

        HttpResponse<String> response =
            client.send(request, BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
