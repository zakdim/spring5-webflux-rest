package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Create by dmitri on 2022-11-20.
 */
@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Vendor> findById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorPublisher) {
        return vendorRepository.saveAll(vendorPublisher).then();
    }

    @PutMapping("/{id}")
    Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {

        return vendorRepository.findById(id)
                .flatMap(found -> {
                    boolean hasChanges = false;
                    if (!Objects.equals(found.getFirstName(), vendor.getFirstName())) {
                        found.setFirstName(vendor.getFirstName());
                        hasChanges = true;
                    }
                    if (!Objects.equals(found.getLastName(), vendor.getLastName())) {
                        found.setLastName(vendor.getLastName());
                        hasChanges = true;
                    }

                    return hasChanges ? vendorRepository.save(found) : Mono.just(found);
                })
                .switchIfEmpty(Mono.just(new Vendor())); // Vendor without ID indicates that it was not found
    }
}
