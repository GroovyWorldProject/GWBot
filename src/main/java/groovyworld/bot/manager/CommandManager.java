package groovyworld.bot.manager;

import groovyworld.bot.commands.everyone.HelpCommand;
import groovyworld.bot.commands.everyone.PingCommand;
import groovyworld.bot.commands.everyone.VersionCommand;
import groovyworld.bot.commands.moder.BanCommand;
import groovyworld.bot.commands.moder.ClearCommand;
import groovyworld.bot.commands.moder.KickCommand;
import groovyworld.bot.commands.moder.UnbanCommand;
import groovyworld.bot.commands.moder.request.AcceptCommand;
import groovyworld.bot.commands.moder.request.DeclineCommand;
import groovyworld.bot.commands.music.*;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {
    private final Map<String, Commands> commands = new HashMap<>();

    public CommandManager() {
        register(new PingCommand());
        register(new HelpCommand(this));
        register(new KickCommand());
        register(new BanCommand());
        register(new UnbanCommand());
        register(new JoinCommand());
        register(new LeaveCommand());
        register(new PlayCommand());
        register(new StopCommand());
        register(new QueueCommand());
        register(new SkipCommand());
        register(new NowPlayingCommand());
        register(new VersionCommand());
        register(new ClearCommand());
        register(new AcceptCommand());
        register(new DeclineCommand());
    }

    private void register(Commands commandClass) {
        if (!commands.containsKey(commandClass.setCommandName())) {
            commands.put(commandClass.setCommandName(), commandClass);
        }
    }

    public Collection<Commands> getCommands() {
        return commands.values();
    }

    public Commands getCommand(@NotNull String name) {
        return commands.get(name);
    }

    void handleCommand(GuildMessageReceivedEvent event) {
//        final String prefix = Const.PREFIXES.get(event.getGuild().getIdLong());

        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(Const.prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            event.getChannel().sendTyping().queue();
            commands.get(invoke).handle(ctx);
        }
    }
}