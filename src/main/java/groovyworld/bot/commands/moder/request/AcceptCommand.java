package groovyworld.bot.commands.moder.request;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.List;

public class AcceptCommand implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Member member = ctx.getMember();

        if (ctx.getArgs().isEmpty()) {
            EmbedBuilder notFoundArguments = new EmbedBuilder();
            notFoundArguments
                    .setColor(Color.RED)
                    .setTitle("Неудача. Нет аргументов")
                    .setDescription("Использование команды: `" + Const.prefix + setCommandName() + " <id пользователя>` ");
            channel.sendMessageEmbeds(notFoundArguments.build()).queue();
        }

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder userNoArgs = new EmbedBuilder();
            userNoArgs
                    .setColor(0xff3923)
                    .setTitle("Неудача. Отсутсвует разрешение")
                    .setDescription("У вас нет разрешения: \"Администратор\"");
            channel.sendMessageEmbeds(userNoArgs.build()).queue();
        }

        String argsJoined = String.join(" ", ctx.getArgs());

        User user = ctx.getSelfUser();

        user.openPrivateChannel().flatMap(a -> {
            if (argsJoined.isEmpty()) {
                EmbedBuilder notFoundArguments = new EmbedBuilder();
                notFoundArguments
                        .setColor(Color.RED)
                        .setTitle("Неудача. Нет аргументов")
                        .setDescription("Использование команды: `" + Const.prefix + setCommandName() + " <id пользователя>` ");
                channel.sendMessageEmbeds(notFoundArguments.build()).queue();
            }

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(Color.CYAN).setTitle("Система оповещений GroovyWorld")
                    .setDescription(
                            "Доброго времени суток! " +
                                    "Я бот проекта GroovyWorld. " +
                                    "Хочу сообщить, что ваша заявка была рассмотрена и одобренна администрацией! " +
                                    "Вот ваша ссылка, на проект, обязательно ознакомьтесь с правилами! \n" +
                                    channel.createInvite().setMaxUses(1).complete().getUrl() + " \n" +
                                    "Приятной игры на GroovyWorld!"
                    );

            return channel.sendMessageEmbeds(success.build());
        }).queue();
    }

    @Override
    public String getHelp() {
        return "null";
    }

    @Override
    public String getHelpUsage() {
        return "null";
    }

    @Override
    public String setCommandName() {
        return "accept";
    }
}
