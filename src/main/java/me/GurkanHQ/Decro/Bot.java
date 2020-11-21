package me.GurkanHQ.Decro;



import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;



public class Bot {



    private Bot() throws LoginException {

        WebUtils.setUserAgent("Mozilla/5.0 Decro#7905 / ThePigStupo#8015");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                .setColor(0xf7ae1b)
                .setFooter("Decro Bot")
        );

        JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS
        )
                .addEventListeners(new Listener())
                .disableCache(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOTE
                )
                .enableCache(
                        CacheFlag.VOICE_STATE
                )
                .setActivity(Activity.watching("=help"))
                .build();





    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }

}
