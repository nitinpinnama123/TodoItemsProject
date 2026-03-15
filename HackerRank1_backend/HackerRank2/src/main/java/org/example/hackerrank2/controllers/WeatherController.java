package org.example.hackerrank1.controllers;

import org.example.hackerrank2.objects.WeatherRecord;
import org.example.hackerrank2.objects.WeatherDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.hackerrank1.services.BookService;

import java.util.List;


@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping
    public ResponseEntity<WeatherRecord> createRecord(@Valid @RequestBody WeatherDTO dto) {
        return new ResponseEntity<>(weatherService.save(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WeatherRecord>> getRecords(
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) String region) {
        return ResponseEntity.ok(weatherService.search(cityName, region));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherRecord> getById(@PathVariable Long id) {
        return weatherService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Explicitly handling prohibited methods to return 405
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Void> methodNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }
}