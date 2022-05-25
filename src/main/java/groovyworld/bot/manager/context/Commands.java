package groovyworld.bot.manager.context;

import groovyworld.bot.manager.CommandContext;

public interface Commands {
    void handle(CommandContext ctx);
    String getHelp();
    String getHelpUsage();
    String setCommandName();
}