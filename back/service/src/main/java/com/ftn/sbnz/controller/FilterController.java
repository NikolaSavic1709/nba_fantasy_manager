package com.ftn.sbnz.controller;


import com.ftn.sbnz.DTO.AddFilterDTO;
import com.ftn.sbnz.DTO.FilterDTO;
import com.ftn.sbnz.DTO.players.PlayerDTO;
import com.ftn.sbnz.DTO.stats.AddCategoryScoresDTO;
import com.ftn.sbnz.DTO.stats.CategoryScoresDTO;
import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.model.models.stats.CategoryScores;
import com.ftn.sbnz.repository.IFilterRepository;
import com.ftn.sbnz.repository.players.IPlayerRepository;
import com.ftn.sbnz.utils.KieSessionProvider;
import org.apache.commons.io.IOUtils;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/filter")
public class FilterController {

    private final KieContainer kieContainer;

    private final KieSessionProvider kieSessionProvider;
    private final IPlayerRepository playerRepository;
    private final IFilterRepository filterRepository;

    @Autowired
    public FilterController(KieContainer kieContainer, KieSessionProvider kieSessionProvider,
                            IPlayerRepository playerRepository, IFilterRepository filterRepository) {
        this.kieContainer = kieContainer;
        this.kieSessionProvider = kieSessionProvider;
        this.playerRepository = playerRepository;
        this.filterRepository = filterRepository;
    }

    @GetMapping
    public ResponseEntity<?> filter(@RequestParam int startFilter) {

        boolean exists = filterRepository.existsById(startFilter);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid filter ID");
        }

        StartFilter sf = new StartFilter(startFilter);
        kieSessionProvider.getKieSession().insert(sf);

        FilteredList filteredList = new FilteredList();
        kieSessionProvider.getKieSession().insert(filteredList);

        int i = kieSessionProvider.getKieSession().fireAllRules();
        System.out.println(i);

        List<PlayerDTO> playerDTOList = new ArrayList<>();
        for(Player p : filteredList.getPlayers()){
            playerDTOList.add(new PlayerDTO(p));
        }

        return new ResponseEntity<>(playerDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllFilters() {
        List<Filter> filters = filterRepository.findAll();
        List<FilterDTO> filtersDTO = new ArrayList<>();
        for(Filter f : filters){
            filtersDTO.add(new FilterDTO(f));
        }

        return new ResponseEntity<>(filtersDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFilter(@RequestBody AddFilterDTO filterDTO) {

        Filter filter = filterDTO.generateFilter();
        Filter newFilter = filterRepository.save(filter);

        return new ResponseEntity<>(new FilterDTO(newFilter), HttpStatus.OK);
    }


}
