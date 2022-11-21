package guru.springframework.spring5webfluxrest.repositories;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Create by dmitri on 2022-11-20.
 */
public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
