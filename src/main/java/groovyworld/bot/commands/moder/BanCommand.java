package groovyworld.bot.commands.moder;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class BanCommand implements Commands {

    @Override
    public void handle(CommandContext ctx) {
        //0xff3923 - RED
        //0x22ff2a - GREEN
        TextChannel channel = ctx.getChannel();
        Member member = ctx.getMember();
        Member selfMember = ctx.getGuild().getSelfMember();
        List<Member> mentionedMembers = ctx.getMessage().getMentionedMembers();

        if (mentionedMembers.isEmpty()) {
            EmbedBuilder noArgs = new EmbedBuilder();
            noArgs.setColor(0xff3923);
            noArgs.setTitle("Неудачно. Отсутсвуют аргументы").setDescription("Использование команды: `" + Const.prefix + setCommandName() + " <участник> <причина>` ");
            channel.sendMessageEmbeds(noArgs.build()).queue();
            return;
        }

        Member target = mentionedMembers.get(0);
        String reason = String.join(" ", ctx.getArgs().subList(1, ctx.getArgs().size()));

        if (!member.hasPermission(Permission.BAN_MEMBERS) || !member.canInteract(target)) {
            EmbedBuilder userNoArgs = new EmbedBuilder();
            userNoArgs.setColor(0xff3923);
            userNoArgs.setTitle("Неудача. Отсутсвует разрещение").setTitle("У вас нет разрешения: \"Банить участников\"");
            channel.sendMessageEmbeds(userNoArgs.build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.BAN_MEMBERS) || !selfMember.canInteract(target)) {
            EmbedBuilder botNoArgs = new EmbedBuilder();
            botNoArgs.setColor(0xff3923).setTitle("Failure. Missing permissions").setDescription("I can't ban that **user** or **I** don't have the ban members permission");
            channel.sendMessageEmbeds(botNoArgs.build()).queue();
            return;
        }

        ctx.getGuild().ban(target, 0)
                .reason(String.format("User banned by: %#s, with reason: %s", ctx.getAuthor(), reason)).queue();
        EmbedBuilder s = new EmbedBuilder();
        s.setColor(Color.GREEN).setTitle("Success!").setDescription("User " + target.getAsMention() + " banned by: " + member.getAsMention() + " with reason: " + reason);
        channel.sendMessageEmbeds(s.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Bans a user from the server.";
    }

    @Override
    public String getHelpUsage() {
        return "<username/id> <reason>";
    }

    @Override
    public String setCommandName() {
        return "ban";
    }
}