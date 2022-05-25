package groovyworld.bot.commands.moder;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ClearCommand implements Commands {
    //0xff3923 - RED
    //0x22ff2a - GREEN
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Member member = ctx.getMember();
        Member selfMember = ctx.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder noPerm = new EmbedBuilder();
            noPerm.setColor(0xff3923).setTitle("Failure. Missing Argument").setDescription("You need the `Manage Messages` permission to use this command");
            channel.sendMessageEmbeds(noPerm.build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder noBotPerm = new EmbedBuilder();
            noBotPerm.setColor(0xff3923).setTitle("Failure. Missing Argument")
                    .setDescription("I need the `Manage Messages` permission for this command");
            channel.sendMessageEmbeds(noBotPerm.build()).queue();
            return;
        }

        if (ctx.getArgs().isEmpty()) {
            EmbedBuilder noArgs = new EmbedBuilder();
            noArgs.setColor(0xff3923).setTitle("Failure. Missing Argument");
            noArgs.setDescription("Correct usage is: `" + Const.prefix + setCommandName() + " <amount>`");
            channel.sendMessageEmbeds(noArgs.build()).queue();
            return;
        }

        int amount;
        String arg = ctx.getArgs().get(0);

        try {
            amount = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
            EmbedBuilder noArgNumber = new EmbedBuilder();
            noArgNumber.setColor(0xff3923);
            noArgNumber.setTitle("Missing Argument");
            noArgNumber.setDescription("`"+arg+"` is not a valid number");
            channel.sendMessageEmbeds(noArgNumber.build()).queue();

            return;
        }

        if (amount < 1 || amount > 10000) {
            EmbedBuilder leastAmount = new EmbedBuilder();
            leastAmount.setColor(0xff3923);
            leastAmount.setTitle("Missing Argument");
            leastAmount.setDescription("Amount must be at least 1 and at most 10000");
            channel.sendMessageEmbeds(leastAmount.build()).queue();
            return;
        }

        channel.getIterableHistory()
                .takeAsync(amount)
                .thenApplyAsync((messages) -> {
                    List<Message> goodMessages = messages.stream()
                            .filter((m) -> m.getTimeCreated().isBefore(
                                    OffsetDateTime.now().plus(2, ChronoUnit.WEEKS)
                            ))
                            .collect(Collectors.toList());

                    channel.purgeMessages(goodMessages);

                    return goodMessages.size();
                })

                .whenCompleteAsync(
                        (count, thr) ->
                                channel.sendMessageFormat("Deleted `%d` messages", count).queue(
                                (message) -> message.delete().queueAfter(10, TimeUnit.SECONDS)
                        )
                )
                .exceptionally((thr) -> {
                    String cause = "";

                    if (thr.getCause() != null) {
                        cause = " caused by: " + thr.getCause().getMessage();
                    }

                    channel.sendMessageFormat("Error: %s%s", thr.getMessage(), cause).queue();

                    return 0;
                });
    }

    @Override
    public String getHelp() {
        return "Clears the chat with the specified amount of messages.";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName() + " <amount>";
    }

    @Override
    public String setCommandName() {
        return "clear";
    }
}