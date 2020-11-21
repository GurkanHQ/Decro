package me.GurkanHQ.Decro;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }

    public static final Map<Long, String> PREFIXES = new HashMap<>();





}
