package ru.selsup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class CrptApi {
    private final long secondTimeUnit;
    private final int requestLimit;
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicInteger requestCount = new AtomicInteger(0);

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.secondTimeUnit = timeUnit.toSeconds(1);
        this.requestLimit = requestLimit;
    }

    public synchronized void createRequest() {
        this.lock.lock();
        try {
            if (this.requestCount.get() < this.requestLimit) {
                this.requestCount.incrementAndGet();
                System.out.println("Request count: " + this.requestCount.get());
            } else {
                System.out.println("Request limit");
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Setter
    @Getter
    public static class Document {
//        @JsonProperty("description")
        private Map<String, String> description;
//        @JsonProperty("doc_id")
        private String doc_id;
//        @JsonProperty("doc_status")
        private String doc_status;
//        @JsonProperty("doc_type")
        private String doc_type;
//        @JsonProperty("importRequest")
        private boolean importRequest;
//        @JsonProperty("owner_inn")
        private String owner_inn;
//        @JsonProperty("participant_inn")
        private String participant_inn;
//        @JsonProperty("producer_inn")
        private String producer_inn;
//        @JsonProperty("production_date")
        private LocalDate production_date;
//        @JsonProperty("production_type")
        private String production_type;
//        @JsonProperty("products")
        private Products[] products;
//        @JsonProperty("reg_date")
        private LocalDate reg_date;
//        @JsonProperty("reg_number")
        private String reg_number;
    }

    @Setter
    @Getter
    public static class Products {
//        @JsonProperty("certificate_document")
        private Map<String, String> certificate_document;
//        @JsonProperty("certificate_document_date")
        private Map<String, LocalDate> certificate_document_date;
//        @JsonProperty("certificate_document_number")
        private Map<String, String> certificate_document_number;
//        @JsonProperty("owner_inn")
        private Map<String, String> owner_inn;
//        @JsonProperty("producer_inn")
        private Map<String, String> producer_inn;
//        @JsonProperty("production_date")
        private Map<String, LocalDate> production_date;
//        @JsonProperty("tnved_code")
        private Map<String, String> tnved_code;
//        @JsonProperty("uit_code")
        private Map<String, String> uit_code;
//        @JsonProperty("uitu_code")
        private Map<String, String> uitu_code;
    }

    public static class DocumentSigner {
        public void signDocument(Document document, String signature) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String jsonData;
            try {
                jsonData = objectMapper.writeValueAsString(document);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                String apiUrl = "https://ismp.crpt.ru/api/v3/lk/documents/create";
//                String apiUrl = "http://127.0.0.1:8080/__admin/mappings";

                HttpPost httpPost = new HttpPost(apiUrl);
                httpPost.setHeader("Content-Type", "application/json");

                StringEntity stringEntity = new StringEntity(jsonData);
                httpPost.setEntity(stringEntity);

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    HttpEntity responseEntity = response.getEntity();
                    System.out.println(EntityUtils.toString(responseEntity));
                } catch (HttpResponseException e) {
                    System.err.println("HTTP error occurred: " + e.getStatusCode() + e.getMessage());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
