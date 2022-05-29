package groovyworld.bot.commands.moder.request;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import groovyworld.core.GWCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public record DeclineCommand(GWCore core) implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Member member = ctx.getMember();
        List<Member> mentionedMembers = ctx.getMessage().getMentionedMembers();
        Guild guild = ctx.getGuild();

        if (mentionedMembers.isEmpty()) {
            EmbedBuilder notFoundArguments = new EmbedBuilder();
            notFoundArguments
                    .setColor(Color.RED)
                    .setTitle("Неудача. Нет аргументов")
                    .setDescription("Использование команды: `" + Const.prefix + setCommandName() + " <id пользователя>` ");
            channel.sendMessageEmbeds(notFoundArguments.build()).queue();
        }

        Member target = mentionedMembers.get(0);

        if (!member.hasPermission(Permission.ADMINISTRATOR) || !member.canInteract(target)) {
            EmbedBuilder userNoArgs = new EmbedBuilder();
            userNoArgs
                    .setColor(0xff3923)
                    .setTitle("Неудача. Отсутсвует разрешение")
                    .setDescription("У вас нет разрешения: \"Администратор\"");
            channel.sendMessageEmbeds(userNoArgs.build()).queue();
        }

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(Color.RED).setTitle("Система оповещений GroovyWorld")
                .setDescription(
                        "Доброго времени суток! " +
                                "Я бот проекта GroovyWorld. " +
                                "Хочу сообщить, что ваша заявка была рассмотрена и отклонена администрацией. \n" +
                                "Приношу извинения, за предоставленные неудобства. \n" +
                                "Попробуйте написать заявку в следующий раз. " +
                                "Всего вам доброго, До свидания!"
                );

        target.getUser().openPrivateChannel().flatMap(channeled -> channeled.sendMessageEmbeds(success.build())).queue();

        guild.kick(target).queue();
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
        return "decline";
    }
}
