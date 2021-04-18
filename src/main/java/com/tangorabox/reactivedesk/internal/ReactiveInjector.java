package com.tangorabox.reactivedesk.internal;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ReactiveInjector {

    public static Injector createInjector(Module... guiceModules) {
        return createInjector(Arrays.asList(guiceModules));
    }

    public static Injector createInjector(ClassLoader classLoader, Module... guiceModules) {
        return createInjector(classLoader, Arrays.asList(guiceModules));
    }

    public static Injector createInjector(Collection<Module> guiceModules) {
        return createInjector(null, guiceModules);
    }

    public static Injector createInjector(ClassLoader classLoader, Collection<Module> guiceModules) {
        ReactiveDeskModule reactiveDeskModule = new ReactiveDeskModule(classLoader);
        List<Module> modules = new ArrayList<>();
        modules.add(reactiveDeskModule);
        if (guiceModules != null) {
            modules.addAll(guiceModules);
        }
        Injector injector = Guice.createInjector(modules);
        reactiveDeskModule.setInjector(injector);
        return injector;
    }
}
