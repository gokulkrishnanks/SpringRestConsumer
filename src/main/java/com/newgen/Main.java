package com.newgen;

import com.newgen.app.bo.RestFulDevInput;
import com.newgen.app.config.RestTemplateConfig;
import com.newgen.app.facade.AgifyAPIFacade;
import com.newgen.app.facade.RestFulDevFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("Loader.xml");

        // check if beans are loaded
        AgifyAPIFacade obj1 = (AgifyAPIFacade) context.getBean(AgifyAPIFacade.class);
        System.out.println("AgifyAPIFacade: " + obj1.isObjectInitialized());

        RestFulDevFacade obj2 = (RestFulDevFacade) context.getBean(RestFulDevFacade.class);
        System.out.println("RestFulDevFacade: " + obj2.isObjectInitialized());


        // test GET
        System.out.println(obj1.getAgeFromAPI("gokul"));

        // test POST
        RestFulDevInput input = new RestFulDevInput();
        input.setArea("gokul");
        System.out.println(obj2.postToAPI(input));


    }

}