package groovyworld.core;

import groovyworld.bot.event.JoiningToServerEvent;
import groovyworld.bot.manager.CommandManager;
import groovyworld.bot.manager.LocalListener;
import groovyworld.bot.setup.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@SuppressWarnings({"DuplicatedCode", "ConstantConditions"})
public class GWCore extends JavaPlugin implements Listener {
    public static JDA jda;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        try {
            main();
            this.getLogger().info("GWLoader Started");
        } catch (LoginException e) {
            this.getLogger().info("FATAL. RETRYING...");
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Retrying loading...");
        try {
            onEnable();
        } catch (Exception e) {
            this.getLogger().info("FATAL RETRYING. DISABLING PLUGIN");
            throw new IllegalStateException(e);
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(this.getConfig().getString("asPlayerJoined.message", player.displayName().toString()));
    }

    public static void main() throws LoginException {
        CommandManager manager = new CommandManager();
        jda = JDABuilder.createDefault(Const.token)
                .setActivity(Activity.playing("Мои команды -> " + Const.prefix + "help"))
                .addEventListeners(new LocalListener(manager), new JoiningToServerEvent())
                .setAutoReconnect(true)
                .disableCache(EnumSet.of(CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE))
                .enableCache(EnumSet.of(CacheFlag.VOICE_STATE))
                .build();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("city")) {
            if (args.length > 0 && args.length < 3) {
                sender.sendMessage(this.getConfig().getString("city.message.failure.short"));
            } else if (args.length == 3) {
                if (sender instanceof Player player) {
                    sender.sendMessage(this.getConfig().getString("city.message.success"));

                    EmbedBuilder builder = new EmbedBuilder();

                    List<TextChannel> channels = jda.getGuildById(this.getConfig().getString("jda.serverId"))
                            .getTextChannelsByName(this.getConfig().getString("jda.city.channel"), true);

                    for (TextChannel channel : channels) {
                        builder.setColor(Color.decode(this.getConfig().getString("jda.city.message.color")))
                                .setTitle(this.getConfig().getString("jda.city.message.title"))
                                .setDescription(
                                        this.getConfig().getString("jda.city.message.desc")
                                                + " **"
                                                + Arrays.toString(args)
                                                + "**\n"
                                                + "\n **"
                                                + this.getConfig().getString("jda.city.message.author")
                                                + "** "
                                                + player.getName()

                                );
                        sendMsg(channel, builder.build());
                    }
                }
            } else if (args.length > 3) {
                sender.sendMessage(this.getConfig().getString("city.message.failure.large"));
            }

            return true;
        }

        return false;
    }

    static void sendMsg(TextChannel channel, MessageEmbed messageEmbed) {
        channel.sendMessageEmbeds(messageEmbed).queue();
    }

    public static GWCore instance() {
        return JavaPlugin.getPlugin(GWCore.class);
    }
}
