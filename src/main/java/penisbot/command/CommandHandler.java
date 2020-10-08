package penisbot.command;

import penisbot.Bot;

public interface CommandHandler {
    public void handle(Bot bot, Command command);
}
