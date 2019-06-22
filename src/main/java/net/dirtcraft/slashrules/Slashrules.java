package net.dirtcraft.slashrules;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

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
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
