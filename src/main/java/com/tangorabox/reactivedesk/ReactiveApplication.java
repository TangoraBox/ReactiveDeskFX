package com.tangorabox.reactivedesk;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.tangorabox.reactivedesk.internal.ReactiveInjector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Collections;

public abstract class ReactiveApplication extends Application {

    @Override
    public final void start(Stage primaryStage) throws Exception {
        Injector injector = ReactiveInjector.createInjector(getApplicationModules(primaryStage));
        injector.injectMembers(this);
        startReactiveApp(primaryStage);
    }

    protected Collection<Module> getApplicationModules(Stage primaryStage) {
        return Collections.emptyList();
    }

    protected abstract void startReactiveApp(Stage primaryStage);
}
