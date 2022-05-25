package groovyworld.bot.commands.music;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.player.PlayerManager;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URI;
import java.net.URISyntaxException;

public class PlayCommand implements Commands {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if (ctx.getArgs().isEmpty()) {
            //0xff3923 - Red, 0x22ff2a - Green
            EmbedBuilder noLink = new EmbedBuilder();
            noLink.setColor(0xff3923);
            noLink.setTitle("You don't write a `youtube link`");
            noLink.setDescription("Correct usage is `" + getHelpUsage() + "`");
            channel.sendMessageEmbeds(noLink.build()).queue();
            return;
        }

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            EmbedBuilder botNV = new EmbedBuilder();
            botNV.setColor(0xff3923);
            botNV.setTitle("Bot Not in Voice");
            botNV.setDescription("I need to be in a voice channel for this to work");
            channel.sendMessageEmbeds(botNV.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder userNV = new EmbedBuilder();
            userNV.setColor(0xff3923);
            userNV.setTitle("Failure. User Not in Voice");
            userNV.setDescription("You need to be in a voice channel for this command to work");
            channel.sendMessageEmbeds(userNV.build()).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            EmbedBuilder userNBV = new EmbedBuilder();
            userNBV.setColor(0xff3923).setTitle("Failure").setDescription("You need to be in the same voice channel as me for this to work");
            channel.sendMessageEmbeds(userNBV.build()).queue();
            return;
        }

        String link = String.join(" ", ctx.getArgs());

        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getInstance().loadAndPlay(channel, link);
    }

    @Override
    public String setCommandName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays a song";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName() + " <youtube link>";
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}