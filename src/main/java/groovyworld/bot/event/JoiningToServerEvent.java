package groovyworld.bot.event;

import groovyworld.core.GWCore;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class JoiningToServerEvent extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getJDA().getGuildById(GWCore.instance().getConfig().getString("jda.serverId"));
        Member member = event.getMember();
        Role role = guild.getRoleById(GWCore.instance().getConfig().getString("jda.joinedRoleId"));

        guild.addRoleToMember(member, role).queue();
    }
}
