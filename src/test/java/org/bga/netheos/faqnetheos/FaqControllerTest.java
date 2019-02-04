package org.bga.netheos.faqnetheos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bga.netheos.faqnetheos.dto.Faq;
import org.bga.netheos.faqnetheos.repository.FaqRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FaqControllerTest {

    //Required to Generate JSON content from Java objects
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Autowired
    private FaqRepository faqRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void before(){
        Faq gremlins = new Faq("who is guizmo?", "Guizmo is a mogwaï", List.of("movie", "cute", "fantasy"));
        Faq java = new Faq("what is java?", "java is a POO language", List.of("developer", "language", "POO"));
        Faq kotlin = new Faq("what is kotlin?", "kotlin is yet another language on JVM", List.of("developer", "language", "FP"));
        faqRepository.add(gremlins);
        faqRepository.add(java);
        faqRepository.add(kotlin);
    }

    @After
    public void after(){
        faqRepository.getFaqs().clear();
    }

    private Map<String, Object> getStarWars() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("question", "What is the title of \"a New Hope\" in france?");
        requestBody.put("answer", "La guerre des etoiles");
        requestBody.put("tags", List.of("action", "aventure", "fantasy"));
        return requestBody;
    }


    @Test
    public void testAddFaq() throws JsonProcessingException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        //Creating http entity object with request body and headers
        HttpEntity<String> httpEntity =
                new HttpEntity<>(OBJECT_MAPPER.writeValueAsString(getStarWars()), requestHeaders);

        //Invoking the API
        ResponseEntity<Faq> apiResponse = restTemplate
                .withBasicAuth("spike", "gremlins")
                .postForEntity("/faq", httpEntity, Faq.class);

        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
        assertEquals("La guerre des etoiles", apiResponse.getBody().getAnswer());
    }

    @Test
    public void testListFaq(){
        List apiResponse = restTemplate.withBasicAuth("spike", "gremlins")
                .getForObject("/faq", List.class);

        assertNotNull(apiResponse);
        assertEquals(3, apiResponse.size());
        assertEquals("who is guizmo?", ((LinkedHashMap) apiResponse.get(0)).get("question"));
        assertEquals("what is java?", ((LinkedHashMap) apiResponse.get(1)).get("question"));
        assertEquals("what is kotlin?", ((LinkedHashMap) apiResponse.get(2)).get("question"));

    }

    @Test
    public void testSearch01Faq(){
        List apiResponse = restTemplate.withBasicAuth("guizmo", "mogwai")
                .getForObject("/faq/search?search=java", List.class);
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.size());
        assertEquals("what is java?", ((LinkedHashMap) apiResponse.get(0)).get("question"));
    }

    @Test
    public void testSearch02Faq(){
        List apiResponse = restTemplate.withBasicAuth("guizmo", "mogwai")
                .getForObject("/faq/search?search=what", List.class);
        assertNotNull(apiResponse);
        assertEquals(2, apiResponse.size());
        assertEquals("what is java?", ((LinkedHashMap) apiResponse.get(0)).get("question"));
    }

}
