package penisbot.module;

import org.slf4j.Logger;
import penisbot.Bot;

public abstract class BaseModule {

    private final Bot bot;

    public BaseModule(Bot bot) {
        this.bot = bot;
    }

    protected abstract Logger getLogger();

}
