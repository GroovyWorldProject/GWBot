package groovyworld.bot.commands.everyone;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class VersionCommand implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        EmbedBuilder msgVer = new EmbedBuilder();
        msgVer.setColor(0x074ff);
        msgVer.setTitle("Correct Version:");
        msgVer.setDescription("`" + Const.version + "` - является последней версией бота.");
        channel.sendMessageEmbeds(msgVer.build()).queue();
    }

    @Override
    public String setCommandName() {
        return "version";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName();
    }

    @Override
    public String getHelp() {
        return "Отправляет текущую версию бота";
    }
}
