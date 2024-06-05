package com.ftn.sbnz.utils;

import com.ftn.sbnz.model.models.Filter;
import org.drools.template.ObjectDataCompiler;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TemplateLoader {

    public TemplateLoader() {
    }

    public static KieHelper loadFromObjects() {

        InputStream template = TemplateLoader.class.getResourceAsStream("/rules/template/filter-player.drt");
        List<Filter> data = new ArrayList<Filter>();

        data.add(new Filter(10, 100, "Denver Nuggets", 5, 1));

        ObjectDataCompiler converter = new ObjectDataCompiler();
        String drl = converter.compile(data, template);

        //System.out.println(drl);

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

//        String[] drlFiles = {"/rules/backward/style.drl", "/rules/forward/forward.drl"};
//        for (String drlFile : drlFiles) {
//            InputStream drlStream = getClass().getResourceAsStream(drlFile);
//            String drlContent = IOUtils.toString(drlStream, StandardCharsets.UTF_8);
//            kieHelper.addContent(drlContent, ResourceType.DRL);
//        }

        try {
            getResourceFromRuleFiles(kieHelper);
        } catch (IOException e) {
            System.out.println("Can't load rule files");
            throw new RuntimeException(e);
        }

        Results results = kieHelper.verify();
        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)){
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: "+message.getText());
            }

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        return kieHelper;
    }

    public static KieHelper loadFromSpreadsheet() {

        InputStream template = TemplateLoader.class.getResourceAsStream("/rules/template/filter-player.drt");
        InputStream data = TemplateLoader.class.getResourceAsStream("/rules/template/filter-template-data.xls");

        ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        String drl = converter.compile(data, template, 3, 2);

        //System.out.println(drl);

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

//        String[] drlFiles = {"/rules/backward/style.drl", "/rules/forward/forward.drl"};
//        for (String drlFile : drlFiles) {
//            InputStream drlStream = getClass().getResourceAsStream(drlFile);
//            String drlContent = IOUtils.toString(drlStream, StandardCharsets.UTF_8);
//            kieHelper.addContent(drlContent, ResourceType.DRL);
//        }

        try {
            getResourceFromRuleFiles(kieHelper);
        } catch (IOException e) {
            System.out.println("Can't load rule files");
            throw new RuntimeException(e);
        }

        Results results = kieHelper.verify();
        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)){
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: "+message.getText());
            }

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        return kieHelper;
    }

    private static void getResourceFromRuleFiles (KieHelper kieHelper) throws IOException {
        ClassLoader cl =  Thread.currentThread().getContextClassLoader().getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources = resolver.getResources("/rules/**/*.drl") ;
        for (Resource resource: resources){
            kieHelper.addResource(ResourceFactory.newFileResource(resource.getFile()), ResourceType.DRL);
        }

    }

    public static void getNumberOfRules(KieSession ksession) {
        KieBase kBase = ksession.getKieBase();

        int ruleCount = 0;

        for (KiePackage kPackage : kBase.getKiePackages()) {
            ruleCount += kPackage.getRules().size();
        }

        System.out.println("Number of rules in session: " + ruleCount);
    }
}
