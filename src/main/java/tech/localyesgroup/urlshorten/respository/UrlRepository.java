package tech.localyesgroup.urlshorten.respository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.localyesgroup.urlshorten.entities.UrlEntity;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {
}
