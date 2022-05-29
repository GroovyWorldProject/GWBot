package groovyworld.bot.commands.moder;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import groovyworld.core.GWCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
public record BanCommand(GWCore core) implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        //0xff3923 - RED
        //0x22ff2a - GREEN
        TextChannel channel = ctx.getChannel();
        Member member = ctx.getMember();
        Member selfMember = ctx.getGuild().getSelfMember();
        List<Member> mentionedMembers = ctx.getMessage().getMentionedMembers();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (mentionedMembers.isEmpty()) {
            embedBuilder.setColor(0xff3923);
            embedBuilder.setTitle("Неудачно. Отсутсвуют аргументы").setDescription("Использование команды: `" + getHelpUsage() + "` ");
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        Member target = mentionedMembers.get(0);

        if (!member.hasPermission(Permission.BAN_MEMBERS) || !member.canInteract(target)) {
            embedBuilder.setColor(0xff3923);
            embedBuilder.setTitle("Неудача. Отсутсвует разрешение").setTitle("У вас нет разрешения: \"Банить участников\"");
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.BAN_MEMBERS) || !selfMember.canInteract(target)) {
            embedBuilder.setColor(0xff3923).setTitle("Неудача. Missing permissions").setDescription("I can't ban that **user** or **I** don't have the ban members permission");
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        String reasonnotall = String.join(" ", ctx.getArgs().subList(2, ctx.getArgs().size()));
        String reasonall = String.join(" ", ctx.getArgs().subList(3, ctx.getArgs().size()));

        if (ctx.getArgs().get(2).equalsIgnoreCase("discord")) {

            ctx.getGuild().ban(target, 0)
                    .reason(String.format("Забанил: %#s, Причина: %s", ctx.getAuthor(), reasonnotall)).queue();

            embedBuilder.setColor(Color.GREEN).setTitle("Удачно!")
                    .setDescription(
                            "Игрок " + target.getAsMention()
                            + "\nбыл заблокирован по причине: " + reasonnotall
                            + "\nадминистратором: " + member.getAsMention()
                            + "\nтип блокировки: Discord"
                    );
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        }

        if (ctx.getArgs().get(2).equalsIgnoreCase("minecraft")) {
            Player player = core.getServer().getPlayer(ctx.getArgs().get(1));
            player.banPlayer(reasonnotall);

            embedBuilder.setColor(Color.GREEN).setTitle("Удачно!")
                    .setDescription(
                            "Игрок " + player.getName()
                            + "\nбыл заблокирован по причине: " + reasonnotall
                            + "\nадминистратором: " + ctx.getAuthor()
                            + "\nтип блокировки: Minecraft Server"
                    );
            channel.sendMessageEmbeds(embedBuilder.build());
        }

        if (ctx.getArgs().get(3).equalsIgnoreCase("all")) {
            Player player = core.getServer().getPlayer(ctx.getArgs().get(1));
            player.banPlayer(reasonall);

            ctx.getGuild().ban(target, 0)
                    .reason(String.format("Забанил: %#s, Причина: %s", ctx.getAuthor(), reasonall)).queue();

            embedBuilder.setColor(Color.GREEN).setTitle("Success!")
                    .setDescription(
                            "Игрок " + target.getAsMention() + "(в игре: " + player.getName() + ")"
                            + "\nбыл заблокирован по причине: " + reasonnotall
                            + "\nадминистратором: " + member.getAsMention()
                            + "\nтип блокировки: Везде"
                    );
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Блокирует игрока на сервере.";
    }

    @Override
    public String getHelpUsage() {
        return "discord <никнейм/id> <причина>"
                + "\n" + Const.prefix + setCommandName() + "server <никнейм> <причина>"
                + "\n" + Const.prefix + setCommandName() + "all <ник discord/id> <ник в игре> <причина>";
    }

    @Override
    public String setCommandName() {
        return "ban";
    }
}