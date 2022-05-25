package groovyworld.bot.commands.music;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.player.GuildMusicManager;
import groovyworld.bot.player.PlayerManager;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class StopCommand implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            EmbedBuilder botNC = new EmbedBuilder();
            botNC.setColor(0xff3923);
            botNC.setDescription("I need to be in a voice channel for this to work");
            channel.sendMessageEmbeds(botNC.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder userNC = new EmbedBuilder();
            userNC.setColor(0xff3923);
            userNC.setDescription("You need to be in a voice channel for this command to work");
            channel.sendMessageEmbeds(userNC.build()).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            EmbedBuilder userNBC = new EmbedBuilder();
            userNBC.setColor(0xff3923);
            userNBC.setDescription("You need to be in the same voice channel as me for this to work");
            channel.sendMessageEmbeds(userNBC.build()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x22ff2a);
        success.setDescription("The player has been stopped and the queue has been cleared");
        channel.sendMessageEmbeds(success.build()).queue();
    }

    @Override
    public String setCommandName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "Stops the current song and clears the queue";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName();
    }
}