package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository repository;
    VendorController controller;

    @Before
    public void setUp() throws Exception {
        repository = Mockito.mock(VendorRepository.class);
        controller = new VendorController(repository);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void list() {
        BDDMockito.given(repository.findAll()).willReturn(Flux.just(
                Vendor.builder().firstName("Fred").lastName("Flintstone").build(),
                Vendor.builder().firstName("Barney").lastName("Rubble").build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(repository.findById("someID"))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL + "/someID")
                .exchange()
                .expectBody(Vendor.class);
    }
}