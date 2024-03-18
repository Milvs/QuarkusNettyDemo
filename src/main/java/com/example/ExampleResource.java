package com.example;

import com.example.client.Client;
import com.example.serverA.ServerA;
import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ExampleResource {

    @Inject
    ServerA serverA;

    @Inject
    Client client;

    @Startup
    public void init() throws Exception {
        this.serverA.start();
        Thread.sleep(5000);
        this.client.start();

    }




}
