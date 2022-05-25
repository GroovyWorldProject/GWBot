package groovyworld.bot.setup;

import groovyworld.core.GWCore;

public class Const {
    public static final String prefix = GWCore.instance().getConfig().getString("jda.prefix");
    public static final String version = GWCore.instance().getConfig().getString("jda.version");
    public static final String token = GWCore.instance().getConfig().getString("jda.token");
}
