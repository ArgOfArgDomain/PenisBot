package penisbot.module;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import penisbot.Bot;
import penisbot.module.annotation.Module;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

public class ModuleRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Bot bot;
    private final ArrayList<BaseModule> modules = new ArrayList<>();

    public ModuleRepository(Bot bot) {
        this.bot = bot;
    }

    public void loadModules() throws ModuleInitializationException {
        Reflections reflections = new Reflections("penisbot");

        try {
            // Find all classes annotated with @Module
            Set<Class<? extends Object>> moduleClasses = reflections.getTypesAnnotatedWith(Module.class);
            for (Class<? extends Object> moduleClass : moduleClasses) {
                // Verify derives from BaseModule
                if (!BaseModule.class.isAssignableFrom(moduleClass)) {
                    throw new ModuleInitializationException(
                            "Class " + moduleClass.getName() + " annotated as " + Module.class.getName() + " but does not derive from " + BaseModule.class.getName());
                }

                // Find constructor
                Constructor<?> constructor = moduleClass.getConstructor(Bot.class);

                // Build it
                BaseModule module = (BaseModule) constructor.newInstance(bot);
                logger.info("Loaded module: " + module.getModuleName() + " (" + module.getClass().getName() + ")");

                // Store it
                modules.add(module);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Error initializing module", e);
            throw new ModuleInitializationException("Error initializing module", e);
        }
    }

}
