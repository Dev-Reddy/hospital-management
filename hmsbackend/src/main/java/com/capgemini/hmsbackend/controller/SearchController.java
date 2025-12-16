package com.capgemini.hmsbackend.controller;

import com.capgemini.hmsbackend.dto.SearchItemDto;
import com.capgemini.hmsbackend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin // or configure CORS globally
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/patients/search")
    public List<SearchItemDto> searchPatients(@RequestParam("name") String term,
                                              @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return searchService.searchPatients(term, limit);
    }

    @GetMapping("/physicians/search")
    public List<SearchItemDto> searchPhysicians(@RequestParam("name") String term,
                                             @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return searchService.searchPhysicians(term, limit);
    }
    @GetMapping("/nurses/search")
    public List<SearchItemDto> searchNurse(@RequestParam("name") String term,
                                                @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return searchService.searchNurse(term, limit);
    }

}
