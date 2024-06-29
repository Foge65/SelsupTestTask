package ru.selsup;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;

class CrptApiTest {
    @Test()
    void signDocument() throws Exception {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 10);
        crptApi.createRequest();

        CrptApi.DocumentSigner documentSigner = new CrptApi.DocumentSigner();

        CrptApi.Document document = new CrptApi.Document();
        document.setDescription(Map.of("participantInn", "string"));
        document.setDoc_id("string");
        document.setDoc_status("string");
        document.setDoc_type("LP_INTRODUCE_GOODS");
        document.setImportRequest(true);
        document.setOwner_inn("string");
        document.setParticipant_inn("string");
        document.setProducer_inn("string");
        document.setProduction_date(LocalDate.of(2020, 1, 23));
        document.setProduction_type("string");

        CrptApi.Products products = new CrptApi.Products();
        products.setCertificate_document(Map.of("certificate_document", "string"));
        products.setCertificate_document_date(Map.of("certificate_document_date", LocalDate.of(2020, 1, 23)));
        products.setCertificate_document_number(Map.of("certificate_document_number", "string"));
        products.setOwner_inn(Map.of("owner_inn", "string"));
        products.setProducer_inn(Map.of("producer_inn", "string"));
        products.setProduction_date(Map.of("production_date", LocalDate.of(2020, 1, 23)));
        products.setTnved_code(Map.of("tnved_code", "string"));
        products.setUit_code(Map.of("uit_code", "string"));
        products.setUitu_code(Map.of("uitu_code", "string"));
        document.setProducts(new CrptApi.Products[]{products});

        document.setReg_date(LocalDate.of(2020, 1, 23));
        document.setReg_number("string");

        documentSigner.signDocument(document, "signedTest");

//        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.writeValueAsString(CrptApi.Document.class);
//        CloseableHttpResponse httpResponse = Mockito.mock(CloseableHttpResponse.class);
//        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(httpResponse);
    }
}
