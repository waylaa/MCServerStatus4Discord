package com.wayla.mcserverstatus4discord;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

public final class MCServerStatus4Discord extends JavaPlugin {

    private FileConfiguration configuration;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        configuration = this.getConfig();
        configuration.addDefault("WebhookClientUrl", "");
        configuration.addDefault("WebhookClientUsername", "");
        configuration.addDefault("WebhookClientAvatarUrl", "");
        configuration.addDefault("EmbedTitle", "");
        configuration.addDefault("EmbedHexColorWhenServerIsOnline", "");
        configuration.addDefault("EmbedHexColorWhenServerIsOffline", "");
        configuration.addDefault("EmbedDescriptionWhenServerIsOnline", "");
        configuration.addDefault("EmbedDescriptionWhenServerIsOffline", "");

        if (configuration.getString("WebhookClientUrl") == null ||
            configuration.getString("WebhookClientUrl").equals("")) {
            this.getLogger().severe("The webhook URL is empty or invalid. This plugin cannot work without it. " +
                                          "Create a webhook from your discord server's integration settings, " +
                                          " get its URL and paste it in the plugin's configuration file (config.yml) " +
                                          "where it says 'WebhookClientUrl' between 2 single quotes. " +
                                          "Example: WebhookClientUrl: 'YOUR_WEBHOOK_URL'. Also, make sure to fill " +
                                          "every other value inside the configuration file such as the" +
                                          "webhook's username, profile picture, etc.");
            return;
        }

        if (configuration.getString("WebhookClientUsername") == null ||
            configuration.getString("WebhookClientUsername").equals("")) {
            this.getLogger().warning("It is highly recommended to set the webhook a username.");
        }

        if (configuration.getString("WebhookClientAvatarUrl") == null ||
            configuration.getString("WebhookClientAvatarUrl").equals("")) {
            this.getLogger().warning("The avatar URL is empty or invalid. The default one will be used instead.");
        }

        DiscordWebhookClient client = new DiscordWebhookClient(configuration.getString("WebhookClientUrl"));
        client.setUsername(configuration.getString("WebhookClientUsername"));
        client.setAvatarUrl(configuration.getString("WebhookClientAvatarUrl"));

        DiscordWebhookClient.EmbedObject embed = new DiscordWebhookClient.EmbedObject();
        embed.setTitle(configuration.getString("EmbedTitle"));
        embed.setColor(Color.decode(configuration.getString("EmbedHexColorWhenServerIsOnline")));
        embed.setDescription(configuration.getString("EmbedDescriptionWhenServerIsOnline"));

        client.addEmbed(embed);

        try {
            client.execute();
        } catch (IOException e) {
            this.getLogger().severe("Failed to start the webhook client. Exception message: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        DiscordWebhookClient client = new DiscordWebhookClient(configuration.getString("WebhookClientUrl"));
        client.setUsername(configuration.getString("WebhookClientUsername"));
        client.setAvatarUrl(configuration.getString("WebhookClientAvatarUrl"));

        DiscordWebhookClient.EmbedObject embed = new DiscordWebhookClient.EmbedObject();
        embed.setTitle(configuration.getString("EmbedTitle"));
        embed.setColor(Color.decode(configuration.getString("EmbedHexColorWhenServerIsOffline")));
        embed.setDescription(configuration.getString("EmbedDescriptionWhenServerIsOffline"));

        client.addEmbed(embed);

        try {
            client.execute();
        } catch (IOException e) {
            this.getLogger().severe("Failed to start the webhook client. Exception message: " + e.getMessage());
        }
    }
}
