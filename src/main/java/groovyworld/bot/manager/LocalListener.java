package groovyworld.bot.manager;

import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalListener extends ListenerAdapter {
    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(LocalListener.class);

    public LocalListener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info(String.format("Logged in as %#s", event.getJDA().getSelfUser()));
    }

//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//
//        User author = event.getAuthor();
//        Message message = event.getMessage();
//        String content = message.getContentDisplay();
//
//        if (event.isFromType(ChannelType.TEXT)) {
//
//            Guild guild = event.getGuild();
//            TextChannel textChannel = event.getTextChannel();
//        } else if (event.isFromType(ChannelType.PRIVATE)) {
//            logger.info(String.format("[PRIV]<%#s>: %s", author, content));
//        }
//    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String rw = event.getMessage().getContentRaw();

        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && rw.startsWith(Const.prefix)) {
            manager.handleCommand(event);
        }
    }
}
