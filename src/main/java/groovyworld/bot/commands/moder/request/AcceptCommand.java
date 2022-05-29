package groovyworld.bot.commands.moder.request;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import groovyworld.core.GWCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.List;

@SuppressWarnings({"ConstantConditions"})
public record AcceptCommand(GWCore core) implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Member member = ctx.getMember();
        List<Member> mentionedMembers = ctx.getMessage().getMentionedMembers();
        Guild guild = ctx.getGuild();
        Role role = guild.getRoleById(core.getConfig().getString("jda.roleId"));

        if (ctx.getArgs().isEmpty()) {
            EmbedBuilder notFoundArguments = new EmbedBuilder();
            notFoundArguments
                    .setColor(Color.RED)
                    .setTitle("Неудача. Нет аргументов")
                    .setDescription("Использование команды: `" + Const.prefix + setCommandName() + " <пользователь>` ");
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


        EmbedBuilder success = new EmbedBuilder();

        Member target = mentionedMembers.get(0);

        success.setColor(Color.CYAN).setTitle("Система оповещений GroovyWorld")
                .setDescription(
                        "Доброго времени суток, " + target.getAsMention() +
                        "Я бот проекта GroovyWorld. " +
                        "Хочу сообщить,что ваша заявка была рассмотрена и одобренна администрацией! " +
                        "Я выдам вам роль на сервере, для общения на нем. \n" +
                        "Приятной игры на GroovyWorld!"
                );

        target.getUser().openPrivateChannel().flatMap(a -> a.sendMessageEmbeds(success.build())).queue();

        guild.addRoleToMember(target, role).queue();
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
