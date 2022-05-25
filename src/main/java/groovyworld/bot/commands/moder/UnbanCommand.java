package groovyworld.bot.commands.moder;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class UnbanCommand implements Commands {
    //0xff3923 - RED
    //0x22ff2a - GREEN
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        if (!ctx.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder noUserPerms = new EmbedBuilder();
            noUserPerms.setColor(0xff3923);
            noUserPerms.setDescription("You need the Ban Members permission to use this command.");
            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        if (!ctx.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder noBotPerms = new EmbedBuilder();
            noBotPerms.setColor(0xff3923);
            noBotPerms.setDescription("I need the Ban Members permission to unban members.");
            channel.sendMessageEmbeds(noBotPerms.build()).queue();
            return;
        }

        if (ctx.getArgs().isEmpty()) {
            EmbedBuilder noArgs = new EmbedBuilder();
            noArgs.setColor(0xff3923);
            noArgs.setTitle("Missing Argument");
            noArgs.setDescription("Usage: `" + Const.prefix + setCommandName() + " <username/user id/username#id>`");
            channel.sendMessageEmbeds(noArgs.build()).queue();
            return;
        }

        String argsJoined = String.join(" ", ctx.getArgs());

        ctx.getGuild().retrieveBanList().queue((bans) -> {

            List<User> goodUsers = bans.stream().filter((ban) -> isCorrectUser(ban, argsJoined))
                    .map(Guild.Ban::getUser).toList();

            if (goodUsers.isEmpty()) {
                EmbedBuilder noBan = new EmbedBuilder();
                noBan.setColor(0x074ff);
                noBan.setTitle("This user is not banned");
                channel.sendMessageEmbeds(noBan.build()).queue();
                return;
            }

            User target = goodUsers.get(0);

            String mod = String.format("%#s", ctx.getAuthor());
            String bannedUser = String.format("%#s", target);

            EmbedBuilder modUnban = new EmbedBuilder();
            modUnban.setColor(0x074ff);
            modUnban.setTitle("Unbanned By " + mod);

            ctx.getGuild().unban(target)
                    .reason(channel.sendMessageEmbeds(modUnban.build()).toString()).queue();

            EmbedBuilder unbanned = new EmbedBuilder();
            unbanned.setColor(0x22ff2a);
            unbanned.setTitle("Success");
            unbanned.setDescription("`" + bannedUser + "` unbanned.");
            channel.sendMessageEmbeds(unbanned.build()).queue();

        });
    }

    @Override
    public String getHelp() {
        return "Unbans a member from the server";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName() + " <username/id long/username#id>";
    }

    @Override
    public String setCommandName() {
        return "unban";
    }

    private boolean isCorrectUser(Guild.Ban ban, String arg) {
        User bannedUser = ban.getUser();

        return bannedUser.getName().equalsIgnoreCase(arg) || bannedUser.getId().equals(arg)
                || String.format("%#s", bannedUser).equalsIgnoreCase(arg);
    }
}