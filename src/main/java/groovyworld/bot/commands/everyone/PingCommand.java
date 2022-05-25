package groovyworld.bot.commands.everyone;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        EmbedBuilder pingPang = new EmbedBuilder();
        pingPang.setTitle("Понг!").setDescription("Пинг веб-сокета: `" + jda.getGatewayPing()+ "`");

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                        .sendMessageEmbeds(pingPang.build()).queue()
        );
    }

    @Override
    public String getHelp() {
        return "Shows the correct ping from the bot to the discord server";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName();
    }

    @Override
    public String setCommandName() {
        return "ping";
    }
}