package com.example.app1.service;

import lombok.val;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

@Service
public class App1Serice {

    String baseUrl = "http://localhost:9000/app2";

    StringBuilder stringBuilder  = new StringBuilder(baseUrl);


    String PRODUCTS  = "/all";

    String INSERT  = "/insert";

    @Autowired
    private RestTemplate restTemplate;

    public List<Map<String,Object>> getAll() {
        HttpEntity<Void> httpEntity  = new HttpEntity<>(gethttpHeaders());
        String url = stringBuilder.append(PRODUCTS).toString();
        val response  =  restTemplate.exchange(url , HttpMethod.GET ,httpEntity,List.class); ;
        return  response.getBody();
    }

    public String insert(String name) throws UnsupportedEncodingException {

        byte[] compressedBody = compress(name.getBytes());
        String s1 = new String(compressedBody, "UTF-8");
        System.out.println(s1);
        HttpEntity <String> httpEntity  = new HttpEntity<>(s1,gethttpHeaders());
        String url = stringBuilder.append(INSERT).toString();
        val response  =  restTemplate.exchange(url , HttpMethod.POST ,httpEntity,String.class); ;



        return response.getBody();
    }


    public Map<String,String> insert1(Map<String,String> payload) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(payload);
        byte[] compressedBody = compress(jsonString.getBytes());
        String s1 = new String(compressedBody, "UTF-8");
        //String utf8String = new String(byteArray, StandardCharsets.UTF_8);

        // Create a Map to store the UTF-8 encoded string
        Map<String, String> map = new HashMap<>();
        map.put("s1Key", s1);
        System.out.println(s1);
        System.out.println(map);
        HttpEntity <Map<String,String>> httpEntity  = new HttpEntity<>(map,gethttpHeaders());
        String url = stringBuilder.append(INSERT).toString();
        val response  =  restTemplate.exchange(url , HttpMethod.POST ,httpEntity,Map.class); ;

        return response.getBody();
    }


    private HttpHeaders gethttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
        return headers;
    }

    private byte[] compress(byte[] data) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {

            gzipStream.write(data);
            gzipStream.finish();

            return byteStream.toByteArray();
        } catch (IOException e) {
            // Handle compression failure
            e.printStackTrace();
            return new byte[0];
        }
    }
}
