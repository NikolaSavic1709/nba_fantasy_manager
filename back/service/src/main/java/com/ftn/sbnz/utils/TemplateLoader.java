package com.ftn.sbnz.utils;

import com.ftn.sbnz.model.models.Filter;
import com.ftn.sbnz.model.models.stats.CategoryScores;
import com.ftn.sbnz.model.repository.ICategoryScoresRepository;
import com.ftn.sbnz.model.repository.IFilterRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class TemplateLoader {


    private IFilterRepository filterRepository;
    private ICategoryScoresRepository categoryScoresRepository;

    @Autowired
    public TemplateLoader(IFilterRepository filterRepository, ICategoryScoresRepository categoryScoresRepository) {
        this.filterRepository = filterRepository;
        this.categoryScoresRepository = categoryScoresRepository;
    }

    public KieHelper loadFromObjects() {
        KieHelper kieHelper = new KieHelper();

        try {
            List<Filter> filters = filterRepository.findAll();
            compileAndAddTemplateFromObjects(kieHelper, "/rules/template/filter-player.drt", filters);

            List<CategoryScores> categoryScores = categoryScoresRepository.findAll();
            compileAndAddTemplateFromObjects(kieHelper, "/rules/template/category-scores.drt", categoryScores);

            getResourceFromRuleFiles(kieHelper);

            Results results = kieHelper.verify();
            if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {
                List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
                for (Message message : messages) {
                    System.out.println("Error: " + message.getText());
                }

                throw new IllegalStateException("Compilation errors were found. Check the logs.");
            }

        } catch (IOException e) {
            System.out.println("Can't load rule files");
            throw new RuntimeException(e);
        }

        return kieHelper;
    }

    private static void compileAndAddTemplateFromObjects(KieHelper kieHelper, String templatePath, List<?> data) {
        InputStream template = TemplateLoader.class.getResourceAsStream(templatePath);

        ObjectDataCompiler converter = new ObjectDataCompiler();
        String drl = converter.compile(data, template);

        kieHelper.addContent(drl, ResourceType.DRL);
    }


    public static KieHelper loadFromSpreadsheets() {
        KieHelper kieHelper = new KieHelper();

        try {
            compileAndAddTemplate(kieHelper, "/rules/template/filter-player.drt", "/rules/template/filter-template-data.xls");
            compileAndAddTemplate(kieHelper, "/rules/template/category-scores.drt", "/rules/template/category-scores-template-data.xls");

            getResourceFromRuleFiles(kieHelper);

            Results results = kieHelper.verify();
            if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)){
                List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
                for (Message message : messages) {
                    System.out.println("Error: " + message.getText());
                }

                throw new IllegalStateException("Compilation errors were found. Check the logs.");
            }

        } catch (IOException e) {
            System.out.println("Can't load rule files");
            throw new RuntimeException(e);
        }

        return kieHelper;
    }

    private static void compileAndAddTemplate(KieHelper kieHelper, String templatePath, String dataPath) {
        InputStream template = TemplateLoader.class.getResourceAsStream(templatePath);
        InputStream data = TemplateLoader.class.getResourceAsStream(dataPath);

        ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        String drl = converter.compile(data, template, 3, 2);

        //System.out.println(drl);
        kieHelper.addContent(drl, ResourceType.DRL);
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
