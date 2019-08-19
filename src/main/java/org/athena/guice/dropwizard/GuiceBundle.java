package org.athena.guice.dropwizard;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.logging.LoggingUtil;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.athena.guice.jersey2.JerseyGuiceModule;
import org.athena.guice.jersey2.JerseyGuiceUtils;
import org.glassfish.hk2.api.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GuiceBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private final Logger logger = LoggerFactory.getLogger(GuiceBundle.class);

    private final AutoConfig autoConfig;
    private final List<Module> modules;
    private final InjectorFactory injectorFactory;
    private Injector baseInjector;
    private DropwizardEnvironmentModule dropwizardEnvironmentModule;
    private Optional<Class<T>> configurationClass;
    private Stage stage;

    public static class Builder<T extends Configuration> {
        private AutoConfig autoConfig;
        private List<Module> modules = Lists.newArrayList();
        private Optional<Class<T>> configurationClass = Optional.absent();
        private InjectorFactory injectorFactory = new InjectorFactoryImpl();

        public Builder<T> addModule(Module module) {
            Preconditions.checkNotNull(module);
            modules.add(module);
            return this;
        }

        public Builder<T> addModules(Module moduleOne, Module moduleTwo,
                                     Module... moreModules) {
            addModule(moduleOne);
            addModule(moduleTwo);
            for (Module module : moreModules) {
                addModule(module);
            }
            return this;
        }

        public Builder<T> setConfigClass(Class<T> clazz) {
            configurationClass = Optional.of(clazz);
            return this;
        }

        public Builder<T> setInjectorFactory(InjectorFactory factory) {
            Preconditions.checkNotNull(factory);
            injectorFactory = factory;
            return this;
        }

        public Builder<T> enableAutoConfig(String... basePackages) {
            Preconditions.checkArgument(basePackages.length > 0, "at least one package must "
                    + "be specified for AutoConfig");
            Preconditions.checkArgument(autoConfig == null, "autoConfig already enabled!");
            autoConfig = new AutoConfig(basePackages);
            return this;
        }

        public GuiceBundle<T> build() {
            return build(Stage.PRODUCTION);
        }

        public GuiceBundle<T> build(Stage s) {
            return new GuiceBundle<>(s, autoConfig, modules, configurationClass, injectorFactory);
        }

    }

    public static <T extends Configuration> Builder<T> newBuilder() {
        return new Builder<>();
    }

    private GuiceBundle(Stage stage, AutoConfig autoConfig, List<Module> modules,
                        Optional<Class<T>> configurationClass, InjectorFactory injectorFactory) {
        Preconditions.checkNotNull(modules);
        Preconditions.checkArgument(!modules.isEmpty());
        Preconditions.checkNotNull(stage);
        this.modules = modules;
        this.autoConfig = autoConfig;
        this.configurationClass = configurationClass;
        this.injectorFactory = injectorFactory;
        this.stage = stage;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        if (configurationClass.isPresent()) {
            dropwizardEnvironmentModule = new DropwizardEnvironmentModule<>(configurationClass.get());
        } else {
            dropwizardEnvironmentModule = new DropwizardEnvironmentModule<>(Configuration.class);
        }
        modules.add(dropwizardEnvironmentModule);
        modules.add(new ServletModule());

        initInjector();
        JerseyGuiceUtils.install((name, parent) -> {
            if (!name.startsWith("__HK2_Generated_")) {
                return null;
            }

            return baseInjector.createChildInjector(new JerseyGuiceModule(name))
                    .getInstance(ServiceLocator.class);
        });

        if (autoConfig != null) {
            autoConfig.initialize(bootstrap, baseInjector.createChildInjector(new JerseyGuiceModule(JerseyGuiceUtils
                    .newServiceLocator())));
        }
    }

    private void initInjector() {
        try {
            baseInjector = injectorFactory.create(this.stage, ImmutableList.copyOf(this.modules));
        } catch (Exception ie) {
            /*
            Injector creation failed so we don't know what state we're in. Graceful shutdown might not
            work if the injector created any non-daemon threads before failing. Seems like the safest
            thing is to System.exit
             */
            logger.error("Exception occurred when creating Guice Injector - exiting", ie);

            // attempt to flush any async logging before exiting
            try {
                LoggingUtil.getLoggerContext().reset();
            } finally {
                System.exit(1);
            }
        }
    }

    @Override
    public void run(final T configuration, final Environment environment) {
        JerseyUtil.registerGuiceBound(baseInjector, environment.jersey());
        JerseyUtil.registerGuiceFilter(environment);
        environment.servlets().addServletListeners(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return baseInjector;
            }
        });
        setEnvironment(configuration, environment);

        if (autoConfig != null) {
            autoConfig.run(environment, baseInjector);
        }
    }

    @SuppressWarnings("unchecked")
    private void setEnvironment(final T configuration, final Environment environment) {
        dropwizardEnvironmentModule.setEnvironmentData(configuration, environment);
    }

    public Injector getInjector() {
        Preconditions.checkState(baseInjector != null, "injector is only available after "
                + "com.hubspot.dropwizard.guice.GuiceBundle.initialize() is called");
        return baseInjector.createChildInjector(new JerseyGuiceModule(JerseyGuiceUtils.newServiceLocator()));
    }

}
