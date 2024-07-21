package tech.localyesgroup.urlshorten.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.localyesgroup.urlshorten.dto.ShortenUrlRequest;
import tech.localyesgroup.urlshorten.dto.ShortenUrlResponse;
import tech.localyesgroup.urlshorten.entities.UrlEntity;
import tech.localyesgroup.urlshorten.respository.UrlRepository;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class Urlcontroller {


    private UrlRepository urlRepository;

    public Urlcontroller(UrlRepository urlRepository) {this.urlRepository = urlRepository;
    }


    @PostMapping(value = "/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request,
                                           HttpServletRequest servletRequest){


        String id;
        do{
            id = RandomStringUtils.randomAlphanumeric(5, 10);

        } while (urlRepository.existsById(id));

        urlRepository.save(new UrlEntity(id, request.url(), LocalDateTime.now().plusMinutes(1)));

        var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", id);

        return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
    }

    @GetMapping("{id}")

    public ResponseEntity<Void> redirect(@PathVariable("id") String id){

        var url = urlRepository.findById(id);

        if (url.isEmpty()){

            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getFullUrl()));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }


}