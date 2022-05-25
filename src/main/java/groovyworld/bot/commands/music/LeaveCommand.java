package groovyworld.bot.commands.music;

import groovyworld.bot.manager.CommandContext;
import groovyworld.bot.manager.context.Commands;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            //0xff3923 - Red, 0x22ff2a - Green
            EmbedBuilder botNotVoice = new EmbedBuilder();
            botNotVoice.setColor(0xff3923);
            botNotVoice.setDescription("I need to be in a voice channel for this to work");
            channel.sendMessageEmbeds(botNotVoice.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder userNotVoice = new EmbedBuilder();
            userNotVoice.setColor(0xff3923);
            userNotVoice.setDescription("You need to be in a voice channel for this command to work");
            channel.sendMessageEmbeds(userNotVoice.build()).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            EmbedBuilder userNinBV = new EmbedBuilder();
            userNinBV.setColor(0xff3923);
            userNinBV.setDescription("You need to be in the same voice channel as me for this to work");
            channel.sendMessageEmbeds(userNinBV.build()).queue();
            return;
        }


        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.closeAudioConnection();
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x22ff2a);
        success.setDescription("I leave from `\uD83D\uDD0A " +memberChannel.getName()+ "` channel");
        channel.sendMessageEmbeds(success.build()).queue();
    }

    @Override
    public String setCommandName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "leave bot from voice channel";
    }
    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName();
    }
}
