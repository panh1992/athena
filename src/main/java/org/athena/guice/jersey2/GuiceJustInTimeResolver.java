package org.athena.guice.jersey2;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.internal.MoreTypes;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.JustInTimeInjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * A {@link JustInTimeInjectionResolver} that is backed by {@link Guice}.
 */
public class GuiceJustInTimeResolver implements JustInTimeInjectionResolver {

    private final ServiceLocator locator;

    private final Injector injector;

    public GuiceJustInTimeResolver(ServiceLocator locator, Injector injector) {
        this.locator = locator;
        this.injector = injector;
    }

    @Override
    public boolean justInTimeResolution(Injectee injectee) {
        Type type = injectee.getRequiredType();
        Class<?> clazz = MoreTypes.getRawType(type);
        if (clazz != null) {
            Binding<?> binding = findBinding(injectee);
            if (binding != null) {
                Key<?> key = binding.getKey();
                Set<Annotation> qualifiers = BindingUtils.getQualifiers(key);
                GuiceBindingDescriptor<?> descriptor = new GuiceBindingDescriptor<>(
                        type, clazz, qualifiers, binding);
                ServiceLocatorUtilities.addOneDescriptor(locator, descriptor);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a {@link Guice} {@link Binding} for the given HK2 {@link Injectee}
     * or {@code null} if there is no such binding (i.e. Guice doesn't have it and
     * doesn't know how to build it).
     */
    private Binding<?> findBinding(Injectee injectee) {
        Key<?> key = BindingUtils.toKey(injectee);
        if (key == null) {
            return null;
        }
        // We can't do anything about HK2 @Contracts
        if (BindingUtils.isHk2Contract(injectee)) {
            return null;
        }
        // Classes with a @Contract annotation are SPIs. They either exist or
        // not. They must be explicit bindings in the world of Guice.
        if (BindingUtils.isJerseyContract(injectee)) {
            return injector.getExistingBinding(key);
        }
        // We've to use Injector#getBinding() to cover Just-In-Time bindings
        // which may fail with an Exception because Guice doesn't know how to
        // construct the requested object.
        return injector.getBinding(key);
    }
}