package groovyworld.bot.commands.everyone;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.CommandManager;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public record HelpCommand(CommandManager manager) implements Commands {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        if (ctx.getArgs().isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            StringBuilder builder = new StringBuilder();

            builder.append("Список команд: \n");
            manager.getCommands().stream().map(Commands::setCommandName).forEach(
                    (it) -> builder.append('`').append(Const.prefix).append(it).append("`\n")
            );
            builder.append("Посмотреть, как использовать команду: `").append(Const.prefix).append(setCommandName()).append(" [команда]`");

            embedBuilder.setColor(Color.GRAY).setDescription(builder.toString());

            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }
        String search = ctx.getArgs().get(0);
        Commands command = manager.getCommand(search);

        if (command == null) {
            EmbedBuilder cmdNull = new EmbedBuilder();
            cmdNull.setColor(0xff3923).setDescription("Нечего не было найдено, для: " + "`" + search + "`");
            channel.sendMessageEmbeds(cmdNull.build()).queue();
            return;
        }
        EmbedBuilder help = new EmbedBuilder();
        help.setColor(Color.GRAY).setTitle("Использование команды").setDescription("Описание: `" + command.getHelp() + "` \n" + "Использование: `" + Const.prefix + command.setCommandName() + " " + command.getHelpUsage() + "`");
        channel.sendMessageEmbeds(help.build()).queue();
    }

    @Override
    public String setCommandName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Отправляет список команд бота";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName() + " [Command]";
    }
}
