package com.mccritz.kmain.teams.commands;

import com.mccritz.kmain.kMain;
import com.mccritz.kmain.profiles.Profile;
import com.mccritz.kmain.profiles.ProfileManager;
import com.mccritz.kmain.teams.Team;
import com.mccritz.kmain.teams.TeamManager;
import com.mccritz.kmain.utils.MessageManager;
import com.mccritz.kmain.utils.PlayerUtility;
import com.mccritz.kmain.utils.command.BaseCommand;
import com.mccritz.kmain.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTeamCommand extends BaseCommand {

    private kMain main = kMain.getInstance();
    private TeamManager tm = main.getTeamManager();
    private ProfileManager pm = main.getProfileManager();
    private List<TeamSubCommand> commands = new LinkedList<>();

    List<String> list = Arrays.asList("create", "join", "leave", "info", "roster", "chat", "hq", "rally", "ff", "password", "kick", "promote", "demote", "sethq", "setrally", "list");

    String[] teamUsage = {
            "&7*** &3Members &7***",
            "&7/team create <name> <password> &3- Create a new team",
            "&7/team join <name> <password> &3- Join a team",
            "&7/team leave &3- Leave your current team",
            "&7/team info <player> &3- Shows information about a player's team",
            "&7/team roster <team> &3- Shows information about a given team",
            "&7/team chat &3- Toggle team chat mode",
            "&7/team hq &3- Teleports you to your team's headquarters",
            "&7/team rally &3- Teleports you to your team's rally point",
            "&7/team list &3- Shows the top 5 tteams by players.",
            "&7*** &3Managers Only &7***",
            "&7/team ff <on/off> &3- Toggle friendly fire",
            "&7/team password <password/none> &7- Sets your team's password",
            "&7/team kick <player> &3- Kicks a player from the team",
            "&7/team promote <player> &3- Promote a player to manager",
            "&7/team demote <player> &3- Demote a player to member",
            "&7/team sethq &3- Sets the team's headquarters",
            "&7/team setrally &3- Sets the team's rally point"};

    public BaseTeamCommand() {
        super("team", null, CommandUsageBy.PlAYER, "t");

        setMinArgs(1);
        setMaxArgs(3);

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < teamUsage.length; i++) {
            if (i < teamUsage.length - 1) {
                builder.append(teamUsage[i]).append('\n');
            } else {
                builder.append(teamUsage[i]);
            }
        }

        setUsage(builder.toString());

        commands.add(new Chat());
        commands.add(new Create());
        commands.add(new Demote());
        commands.add(new Hq());
        commands.add(new Info());
        commands.add(new Join());
        commands.add(new Kick());
        commands.add(new Leave());
        commands.add(new Promote());
        commands.add(new Rally());
        commands.add(new Roster());
        commands.add(new SetFriendlyFire());
        commands.add(new SetHq());
        commands.add(new SetPassword());
        commands.add(new SetRally());
        commands.add(new com.mccritz.kmain.teams.commands.List());
    }

    public TeamSubCommand getSubCommand(String key) {
        TeamSubCommand tc;

        for (TeamSubCommand sub : commands) {
            if (sub.getName().equalsIgnoreCase(key)) {
                tc = sub;
                return tc;
            } else {
                if (sub.getAliases().contains(key.toLowerCase())) {
                    tc = sub;
                    return tc;
                }
            }
        }

        return null;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            MessageManager.message(sender, "&7*** &3Members &7***");
            MessageManager.message(sender, "&7/team create <name> <password>  &3- &bCreate a new team");
            MessageManager.message(sender, "&7/team join <name> <password> &3- &bJoin a team");
            MessageManager.message(sender, "&7/team leave &3- &bLeave your current team");
            MessageManager.message(sender, "&7/team info <player> &3- &b&bShows information about a player's team");
            MessageManager.message(sender, "&7/team roster <team> &3- &bShows information about a given team");
            MessageManager.message(sender, "&7/team chat &3- &bToggle team chat mode");
            MessageManager.message(sender, "&7/team hq &3- &bTeleports you to your team's headquarters");
            MessageManager.message(sender, "&7/team rally &3- &bTeleports you to your team's rally point");
            MessageManager.message(sender, "&7/team list &3- &bShows the top 5 teams by players.");

            MessageManager.message(sender, "&7*** &3Managers Only &7***");
            MessageManager.message(sender, "&7/team ff <on/off> &3- &bToggle friendly fire");
            MessageManager.message(sender, "&7/team password <password/none> &3- &bSets your team's password");
            MessageManager.message(sender, "&7/team kick <player> &3- &bKicks a player from the team");
            MessageManager.message(sender, "&7/team promote <player> &3- &bPromote a player to manager");
            MessageManager.message(sender, "&7/team demote <player> &3- &bDemote a player to member");
            MessageManager.message(sender, "&7/team sethq &3- &bSets the team's headquarters");
            MessageManager.message(sender, "&7/team setrally &3- &bSets the team's rally point");
        } else {
            try {
                TeamSubCommand tc = getSubCommand(args[0]);

                if (tc == null) {
                    MessageManager.message(sender, "&7Unknown team command. Type &c/team &7for help.");
                    return;
                }

                tc.execute(((Player) sender), fixArgs(args));
            } catch (Exception ex) {
                MessageManager.message(sender, "&cAn unexpected error occured: " + ex.getLocalizedMessage() + "\nContact an admin!");
                ex.printStackTrace();
            }
        }
    }

    public List<String> tabComplete(String[] args, CommandSender sender) {
        Collections.sort(list);

        if ((sender instanceof Player)) {
            Player p = (Player) sender;

            if (args.length == 0) {
                return list;
            }

            if (args.length == 1) {
                return list.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("friendlyfire") || args[0].equalsIgnoreCase("ff") || args[0].equalsIgnoreCase("setfriendlyfire") || args[0].equalsIgnoreCase("setff")) {
                    List<String> options = Arrays.asList("true", "on", "off", "false");
                    List<String> list2return = options.stream().filter(opt -> opt.toLowerCase().startsWith(args[1])).collect(Collectors.toList());
                    Collections.sort(list2return);
                    return list2return;
                }

                if (args[0].equalsIgnoreCase("i") || args[0].equalsIgnoreCase("info")) {
                    List<String> list2return = PlayerUtility.toList(PlayerUtility.getOnlinePlayers()).stream().filter(opt -> opt.toLowerCase().startsWith(args[1])).collect(Collectors.toList());
                    Collections.sort(list2return);
                    return list2return;
                }

                if (args[0].equalsIgnoreCase("p") || args[0].equalsIgnoreCase("promote")) {
                    List<String> list2return = new ArrayList<>();
                    Team t = tm.getTeam(p.getUniqueId());
                    for (UUID id : t.getMembers()) {
                        Profile prof = pm.getProfile(id);
                        if (prof.getName().toLowerCase().startsWith(args[1])) {
                            list2return.add(prof.getName());
                        }
                    }
                    Collections.sort(list2return);
                    return list2return;
                }

                if (args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("demote")) {
                    List<String> list2return = new ArrayList<>();
                    Team t = tm.getTeam(p.getUniqueId());
                    for (UUID id : t.getManagers()) {
                        Profile prof = pm.getProfile(id);
                        if (prof.getName().toLowerCase().startsWith(args[1])) {
                            list2return.add(prof.getName());
                        }
                    }

                    Collections.sort(list2return);
                    return list2return;
                }

                if (args[0].equalsIgnoreCase("k") || args[0].equalsIgnoreCase("kick")) {
                    List<String> list2return = new ArrayList<>();
                    Team t = tm.getTeam(p.getUniqueId());
                    for (UUID id : t.getManagers()) {
                        Profile prof = pm.getProfile(id);
                        if (prof.getName().toLowerCase().startsWith(args[1])) {
                            list2return.add(prof.getName());
                        }
                    }
                    for (UUID id : t.getMembers()) {
                        Profile prof = pm.getProfile(id);
                        if (prof.getName().toLowerCase().startsWith(args[1])) {
                            list2return.add(prof.getName());
                        }
                    }
                    Collections.sort(list2return);
                    return list2return;
                }

                if (args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("roster")) {
                    List<String> list2return = tm.getTeams().stream().filter(opt -> opt.getName().toLowerCase().startsWith(args[1])).map(Team::getName).collect(Collectors.toList());
                    Collections.sort(list2return);
                    return list2return;
                }
            }
        }
        return list;
    }

    public String[] fixArgs(String[] args) {
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);
        return subArgs;
    }
}