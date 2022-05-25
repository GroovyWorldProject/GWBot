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

@SuppressWarnings("ConstantConditions")
public class JoinCommand implements Commands {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (selfVoiceState.inVoiceChannel()) {
            //0xff3923 - Red, 0x22ff2a - Green
            EmbedBuilder errorInVoice = new EmbedBuilder();
            errorInVoice.setColor(0xff3923);
            errorInVoice.setDescription("I'm already in a voice channel");
            channel.sendMessageEmbeds(errorInVoice.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder errorUNV = new EmbedBuilder();
            errorUNV.setColor(0xff3923);
            errorUNV.setDescription("You need to be in a voice channel for this command to work");
            channel.sendMessageEmbeds(errorUNV.build()).queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x22ff2a);
        success.setDescription("Connecting to `\uD83D\uDD0A "+memberChannel.getName()+"`");
        channel.sendMessageEmbeds(success.build()).queue();
    }

    @Override
    public String setCommandName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Makes the bot join your voice channel";
    }

    @Override
    public String getHelpUsage() {
        return Const.prefix + setCommandName();
    }
}
