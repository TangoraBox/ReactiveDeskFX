package com.tangorabox.reactivedesk.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;

import java.util.Objects;

class ReactiveDeskModule extends AbstractModule {

    private Injector injector;
    private final JavaFXBuilderFactory defaultJavaFXBuilderFactory = new JavaFXBuilderFactory();
    private final ClassLoader classLoader;

    public ReactiveDeskModule(ClassLoader classLoader) {
        super();
        this.classLoader = classLoader;
    }

    void setInjector(Injector injector) {
        this.injector = Objects.requireNonNull(injector);
    }

    @Override
    protected void configure() {
        bindFXMLComponentListener();
    }

    private void bindFXMLComponentListener() {
        bindListener(new AbstractMatcher<>() {
            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                return Node.class.isAssignableFrom(typeLiteral.getRawType())
                        && typeLiteral.getRawType().isAnnotationPresent(FXMLView.class);
            }
        }, fxmlComponentTypeListener());
    }

    @SuppressWarnings("unchecked")
    private TypeListener fxmlComponentTypeListener() {
        FXMLInflater loader = new FXMLInflater(this::provideFXMLLoader);
        return new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                ((TypeEncounter<Node>) typeEncounter).register((InjectionListener<Node>) loader::loadNestedFXMLOfComponent);
            }
        };
    }

    @Provides
    protected FXMLLoader provideFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(controllerClazz -> {
            if (injector != null) {
                return injector.getInstance(controllerClazz);
            }
            throw new IllegalStateException("Guice Injector cannot be null!!");
        });
        loader.setBuilderFactory(type -> {
            if (type.isAnnotationPresent(FXMLView.class)) {
                return () -> injector.getInstance(type);
            }
            return defaultJavaFXBuilderFactory.getBuilder(type);
        });
        if (classLoader != null) {
            loader.setClassLoader(classLoader);
        }
        return loader;
    }
}
