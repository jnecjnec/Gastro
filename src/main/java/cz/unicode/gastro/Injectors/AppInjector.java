package cz.unicode.gastro.Injectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        Logger aLogger = LoggerFactory.getLogger("cz.jnec");
        bind(Logger.class).toInstance(aLogger);
    }

}
