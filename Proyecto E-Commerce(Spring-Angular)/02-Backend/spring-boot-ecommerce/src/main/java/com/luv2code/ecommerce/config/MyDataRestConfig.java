package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] theAllowedOrigins;

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager){
            entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        HttpMethod[] theUnsupportedActions = {HttpMethod.DELETE, HttpMethod.POST,
                                              HttpMethod.PUT, HttpMethod.PATCH};

        // Disable HTTP methods for Product: PUT, POST and Delete.
        disableHttpMethods(Product.class, config, theUnsupportedActions);

        // Disable HTTP methods for ProductCategory: PUT, POST and Delete.
        disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);

        // Disable HTTP methods for Country: PUT, POST and Delete.
        disableHttpMethods(Country.class, config, theUnsupportedActions);

        // Disable HTTP methods for State: PUT, POST and Delete.
        disableHttpMethods(State.class, config, theUnsupportedActions);

        // Disable HTTP methods for Order: PUT, POST and Delete.
        disableHttpMethods(Order.class, config, theUnsupportedActions);

        // Call an internal helper method
        exposeId(config);

        // configure cors mapping
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
    }

    private static void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeId(RepositoryRestConfiguration config) {
        // Expose entity ids.

        // Get a list of all entity classes fron the entity manager.
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //  Create an array of the entity type
        List<Class> entityClasses = new ArrayList<>();

        // Get the entity types from the entities
        for (EntityType tempEntityTipe : entities){
            entityClasses.add(tempEntityTipe.getJavaType());
        }

        // Expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}

