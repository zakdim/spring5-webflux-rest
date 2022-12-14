package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;

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
        given(repository.findAll()).willReturn(Flux.just(
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
        given(repository.findById("someID"))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL + "/someID")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void create() {
        given(repository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder()
                .firstName("First Name").lastName("Last Name").build());

        webTestClient.post()
                .uri(VendorController.BASE_URL)
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }
    @Test
    public void update() {
        given(repository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().build());

        webTestClient.put()
                .uri(VendorController.BASE_URL + "/someId")
                .body(vendorMonoToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void patch_whenChanges() {
        given(repository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

        given(repository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("Jim").build()));

        Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jim").build());

        webTestClient.patch()
                .uri(VendorController.BASE_URL + "/someId")
                .body(vendorMonoToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(Vendor.class));
    }

    @Test
    public void patch_whenNoChanges() {
        given(repository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

        given(repository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

        Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jimmy").build());

        webTestClient.patch()
                .uri(VendorController.BASE_URL + "/someId")
                .body(vendorMonoToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(repository).findById(anyString());
        verify(repository, never()).save(any(Vendor.class));
    }

    @Test
    public void patch_whenNotFound() {
        given(repository.findById(anyString())).willReturn(Mono.empty()); // not found

        given(repository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("Jim").build()));

        Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jim").build());

        webTestClient.patch()
                .uri(VendorController.BASE_URL + "/someId")
                .body(vendorMonoToUpdate, Vendor.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class)
                .consumeWith(vendorEntityExchangeResult -> {
                    Vendor response = vendorEntityExchangeResult.getResponseBody();
                    assertThat(response).isNotNull();
                    assertThat(response).extracting(Vendor::getId, Vendor::getFirstName, Vendor::getLastName)
                            .containsExactly(null, null, null);
                });

        verify(repository).findById(anyString());
        verify(repository, never()).save(any(Vendor.class));
    }
}