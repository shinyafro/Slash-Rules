package net.dirtcraft.slashrules;

import com.google.inject.Inject;
import net.dirtcraft.slashrules.Commands.CommandRegistrar;
import net.dirtcraft.slashrules.Data.RulesData;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Plugin(
        id = "slashrules",
        name = "Slashrules",
        description = "a better rules plugin",
        url = "https://github.com/Dirt-Craft",
        authors = {
                "ShinyAfro"
        }
)
public class Slashrules {

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File configDir;

    @Inject
    private Logger logger;

    private static Slashrules instance;
    private static RulesData rulesData;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;
        setRulesData();
        CommandRegistrar.registerCommands();
    }

    public static void setRulesData(){
        rulesData = new RulesData();
    }

    public static Slashrules getInstance() {
        return instance;
    }

    public static RulesData getRulesData() {
        return rulesData;
    }

    public File getConfigDir(){
        return configDir;
    }

    public Logger getLogger(){
        return logger;
    }
}
