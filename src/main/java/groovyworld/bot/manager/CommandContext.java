package groovyworld.bot.manager;

import groovyworld.bot.manager.context.CommandEvents;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public record CommandContext(GuildMessageReceivedEvent event, List<String> args) implements CommandEvents {

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public List<String> getArgs() {
        return this.args;
    }
}
