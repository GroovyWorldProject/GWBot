package groovyworld.bot.commands.moder;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class KickCommand implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        //0xff3923 - Red, 0x22ff2a - Green
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();

        if (ctx.getArgs().size() < 2 || message.getMentionedMembers().isEmpty()){
            EmbedBuilder MA = new EmbedBuilder();
            MA.setColor(0xff3923);
            MA.setTitle("Failure. Missing Argument.");
            MA.setDescription("Correct use: `" + Const.prefix + " kick <member> <reason>`");
            channel.sendMessageEmbeds(MA.build()).queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);

        if(!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)){
            EmbedBuilder noPerm = new EmbedBuilder();
            noPerm.setColor(0xff3923).setTitle("Failure").setDescription("You are missing permission to kick this member");
            channel.sendMessageEmbeds(noPerm.build()).queue();
            return;
        }
        final Member selfMember = ctx.getSelfMember();

        if(!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)){
            EmbedBuilder botNoPerm = new EmbedBuilder();
            botNoPerm.setColor(0xff3923).setTitle("Failure").setDescription("I don't have a permission, to kick this member");
            channel.sendMessageEmbeds(botNoPerm.build()).queue();
            return;
        }
        final String reason = String.join(" ", ctx.getArgs().subList(1, ctx.getArgs().size()));

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x22ff2a).setTitle("Success!").setDescription("User " + target.getAsMention() + " was kicked by " + member.getAsMention() + " with reason: `" + reason + "`");

        EmbedBuilder errorKick = new EmbedBuilder();
        errorKick.setColor(0xff3923);
        errorKick.setDescription("Could not kick s%" + ctx.getMessage());
        ctx.getGuild().kick(target, reason).reason(reason).queue(
                (__) -> channel.sendMessageEmbeds(success.build()).queue(),
                (error) -> channel.sendMessageEmbeds(errorKick.build()).queue()

        );
    }

    @Override
    public String setCommandName() {
        return "kick";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName() + " <user> <reason>";
    }

    @Override
    public String getHelp() {
        return "Kick member from server";
    }
}
