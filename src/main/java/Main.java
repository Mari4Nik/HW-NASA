import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static final String REMOTE_SERVICE_URL =
            "https://api.nasa.gov/planetary/apod?api_key=fDwPdJQdEDoxZ99uSAbJYkeRiTvv0DVDsIhj1ttN";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        CloseableHttpResponse response = httpClient.execute(request);
        ObjectNasa objectNasa = mapper.readValue(response.getEntity().getContent(), ObjectNasa.class);

//        response = httpClient.execute(new HttpGet(objectNasa.getUrl()));
//
//        byte[] img = response.getEntity().getContent().readAllBytes();
//        String fileName = new File(objectNasa.getUrl()).getName();
//
//        try (FileOutputStream out = new FileOutputStream(fileName);
//             BufferedOutputStream bos = new BufferedOutputStream(out)) {
//            bos.write(img, 0, img.length);
//        }

        System.out.println(objectNasa);

        HttpGet request1 = new HttpGet(objectNasa.getUrl());
        CloseableHttpResponse response1 = httpClient.execute(request1);
        String[] arr = objectNasa.getUrl().split("/");
        String file = arr[6];
        HttpEntity entity = response1.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(file);
            entity.writeTo(fos);
            fos.close();
        }


    }
}
