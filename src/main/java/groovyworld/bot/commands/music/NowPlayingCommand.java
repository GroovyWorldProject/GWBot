package groovyworld.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.player.GuildMusicManager;
import groovyworld.bot.player.PlayerManager;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class NowPlayingCommand implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            //0xff3923 - Red, 0x22ff2a - Green
            EmbedBuilder notBV = new EmbedBuilder();
            notBV.setColor(0xff3923);
            notBV.setDescription("I need to be in a voice channel for this to work");
            channel.sendMessageEmbeds(notBV.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder notUV = new EmbedBuilder();
            notUV.setColor(0xff3923);
            notUV.setDescription("You need to be in a voice channel for this command to work");
            channel.sendMessageEmbeds(notUV.build()).queue();
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
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();

        if (track == null) {
            EmbedBuilder trackNull = new EmbedBuilder();
            trackNull.setColor(0x074ff);
            trackNull.setDescription("There is no track playing currently");
            channel.sendMessageEmbeds(trackNull.build()).queue();
            return;
        }

        final AudioTrackInfo info = track.getInfo();
        EmbedBuilder nowPlay = new EmbedBuilder();
        nowPlay.setColor(0x074ff);
        nowPlay.setTitle("Now Playing:");
        nowPlay.setDescription("`" + info.title + "`" + " by " + "`" + info.author + "`" + " \n(Link:" + info.uri + ")`");
        channel.sendMessageEmbeds(nowPlay.build()).queue();
    }

    @Override
    public String setCommandName() {
        return "nowplay";
    }

    @Override
    public String getHelp() {
        return "Shows the currently playing song";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName();
    }
}
