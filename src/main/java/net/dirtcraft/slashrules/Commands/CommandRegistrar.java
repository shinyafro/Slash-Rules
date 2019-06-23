package net.dirtcraft.slashrules.Commands;

import net.dirtcraft.slashrules.Config.Lang;
import net.dirtcraft.slashrules.Config.Perms;
import net.dirtcraft.slashrules.Slashrules;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandRegistrar {

    public static void registerCommands(){

        CommandSpec reload = CommandSpec.builder()
                .description(Text.of(Lang.RELOAD_DESCRIPTION))
                .permission(Perms.RELOAD)
                .executor(new ReloadExecutor())
                .build();

        CommandSpec removeLine = CommandSpec.builder()
                .description(Text.of(Lang.REM_LINE_DESCRIPTION))
                .permission(Perms.REM_LINE)
                .arguments(GenericArguments.integer(Text.of("line")))
                .executor(new RemoveLineExecutor())
                .build();

        CommandSpec addLine = CommandSpec.builder()
                .description(Text.of(Lang.ADD_LINE_DESCRIPTION))
                .permission(Perms.ADD_LINE)
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("text")))
                .executor(new AddLineExecutor())
                .build();

        CommandSpec setLine = CommandSpec.builder()
                .description(Text.of(Lang.SET_LINE_DESCRIPTION))
                .permission(Perms.SET_LINE)
                .arguments(
                        GenericArguments.integer(Text.of("line")),
                        (GenericArguments.remainingJoinedStrings(Text.of("text"))))
                .executor(new SetLineExecutor())
                .build();

        CommandSpec help = CommandSpec.builder()
                .description(Text.of(Lang.HELP_DESCRIPTION))
                .permission(Perms.HELP)
                .executor(new HelpExecutor())
                .build();

        CommandSpec other = CommandSpec.builder()
                .description(Text.of(Lang.OTHER_DESCRIPTION))
                .permission(Perms.OTHER)
                .arguments(
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.optional(GenericArguments.integer(Text.of("line"))))
                .executor(new OtherExecutor())
                .build();

        CommandSpec rules = CommandSpec.builder()
                .description(Text.of(Lang.MAIN_DESCRIPTION))
                .executor(new RulesExecutor())
                .permission(Perms.MAIN)
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("line"))))
                .child(help, "help", "?")
                .child(setLine, "editline", "edit")
                .child(addLine, "addline", "setline", "set", "add")
                .child(removeLine, "removeline", "deleteline", "remove", "delete", "del", "rem")
                .child(other, "other")
                .child(reload, "reload")
                .build();

        Sponge.getCommandManager().register(Slashrules.getInstance(), rules, "rules");
    }
}
