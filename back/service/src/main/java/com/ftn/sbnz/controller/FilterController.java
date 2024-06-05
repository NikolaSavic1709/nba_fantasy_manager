package com.ftn.sbnz.controller;


import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.repository.players.IPlayerRepository;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class FilterController {

    private final KieContainer kieContainer;

    private final KieSession kieSession;
    private final IPlayerRepository playerRepository;

    @Autowired
    public FilterController(KieContainer kieContainer, KieSession kieSession, IPlayerRepository playerRepository) {
        this.kieContainer = kieContainer;
        this.kieSession = kieSession;
        this.playerRepository = playerRepository;
    }

    @GetMapping(value = "/filter_1")
    public String filter1() {
        StartFilter sf = new StartFilter(1);
        kieSession.insert(sf);
        int i = kieSession.fireAllRules();
        System.out.println(i);
        return "filter";
    }

    @GetMapping(value = "/filter_2")
    public String filter2() {
        StartFilter sf = new StartFilter(2);
        kieSession.insert(sf);
        int i = kieSession.fireAllRules();
        System.out.println(i);
        return "filter2";
    }

    @GetMapping(value = "/filter_3")
    public String filter3() {
        StartFilter sf = new StartFilter(3);
        kieSession.insert(sf);
        int i = kieSession.fireAllRules();
        System.out.println(i);
        return "filter3";
    }

    @GetMapping(value = "/filter_4")
    public String filter4() {
        StartFilter sf = new StartFilter(4);
        kieSession.insert(sf);
        int i = kieSession.fireAllRules();
        System.out.println(i);
        return "filter4";
    }

}
