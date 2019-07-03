package net.dirtcraft.slashrules.Commands;

import net.dirtcraft.slashrules.Config.Config;
import net.dirtcraft.slashrules.Config.Lang;
import net.dirtcraft.slashrules.Data.RulesData;
import net.dirtcraft.slashrules.Slashrules;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OtherExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Collection<Player> players = args.getAll("player");
        Optional<Integer> line = args.getOne("line");
        RulesData rulesData = Slashrules.getRulesData();

        for(Player player : players) {
            if (!line.isPresent()) {
                PaginationList.builder()
                        .title(TextSerializers.FORMATTING_CODE.deserialize(Config.PAGINATION_RULES_HEADER))
                        .padding(TextSerializers.FORMATTING_CODE.deserialize(Config.PAGINATION_PADDING))
                        .contents(rulesData.getRules())
                        .build()
                        .sendTo(player);
            } else {
                int rule = line.get() - 1;
                List<Text> rules = rulesData.getRules();
                if (rule < 0) throw new CommandException(Text.of(Lang.EXCEPTION_LINE_VALUE_SMALL));
                if (rule >= rules.size()) throw new CommandException(Text.of(Lang.EXCEPTION_LINE_VALUE_BIG));
                player.sendMessage(rules.get(rule));
            }
        }
        return CommandResult.success();
    }
}
