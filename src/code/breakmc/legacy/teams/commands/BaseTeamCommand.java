package code.breakmc.legacy.teams.commands;

import code.breakmc.legacy.Legacy;
import code.breakmc.legacy.profiles.Profile;
import code.breakmc.legacy.profiles.ProfileManager;
import code.breakmc.legacy.teams.Team;
import code.breakmc.legacy.teams.TeamManager;
import code.breakmc.legacy.utils.MessageManager;
import code.breakmc.legacy.utils.PlayerUtility;
import code.breakmc.legacy.utils.command.BaseCommand;
import code.breakmc.legacy.utils.command.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTeamCommand extends BaseCommand {

    private Legacy main = Legacy.getInstance();
    private TeamManager tm = main.getTeamManager();
    private ProfileManager pm = main.getProfileManager();
    private List<TeamSubCommand> commands = new LinkedList<>();

    List<String> list = Arrays.asList("create", "join", "leave", "info", "roster", "chat", "hq", "rally", "ff", "password", "kick", "promote", "demote", "sethq", "setrally", "list");

    String[] teamUsage = {
            "&7&m***&r &3Members &7&m***&r",
            "&3/team create (name) (password)  &7- Create a new team",
            "&3/team join (name) (password) &7- Join a team",
            "&3/team leave &7- Leave your current team",
            "&3/team info (player) &7- Shows information about a player's team",
            "&3/team roster (team) &7- Shows information about a given team",
            "&3/team chat &7- Toggle team chat mode",
            "&3/team hq &7- Teleports you to your team's headquarters",
            "&3/team rally &7- Teleports you to your team's rally point",
            "&3/team list &7- Shows the top 5 tteams by players.",
            "&7&m***&r &3Managers Only &7&m***&r",
            "&3/team ff (on/off) &7- Toggle friendly fire",
            "&3/team password (password/none) &7- Sets your team's password",
            "&3/team kick (player) &7- Kicks a player from the team",
            "&3/team promote (player) &7- Promote a player to manager",
            "&3/team demote (player) &7- Demote a player to member",
            "&3/team sethq &7- Sets the team's headquarters",
            "&3/team setrally &7- Sets the team's rally point"};

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
        commands.add(new code.breakmc.legacy.teams.commands.List());
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
            MessageManager.sendMessage(sender, "&7&m***&r &3Members &7&m***&r");
            // /team create
            MessageManager.sendMessage(sender, "&3/team create (name) (password)  &7- &bCreate a new team");
            // team join
            MessageManager.sendMessage(sender, "&3/team join (name) (password) &7- &bJoin a team");
            // /team leave
            MessageManager.sendMessage(sender, "&3/team leave &7- &bLeave your current team");
            // /team info
            MessageManager.sendMessage(sender, "&3/team info (player) &7- &b&bShows information about a player's team");
            // /team roster
            MessageManager.sendMessage(sender, "&3/team roster (team) &7- &bShows information about a given team");
            // /team chat
            MessageManager.sendMessage(sender, "&3/team chat &7- &bToggle team chat mode");
            // /team hq
            MessageManager.sendMessage(sender, "&3/team hq &7- &bTeleports you to your team's headquarters");
            // /team rally
            MessageManager.sendMessage(sender, "&3/team rally &7- &bTeleports you to your team's rally point");
            // /team list
            MessageManager.sendMessage(sender, "&3/team list &7- Shows the top 5 teams by players.");

            MessageManager.sendMessage(sender, "&7&m***&r &3Managers Only &7&m***&r");
            // /t ff
            MessageManager.sendMessage(sender, "&3/team ff (on/off) &7- &bToggle friendly fire");
            // /team password
            MessageManager.sendMessage(sender, "&3/team password (password/none) &7- &bSets your team's password");
            // /team kick
            MessageManager.sendMessage(sender, "&3/team kick (player) &7- &bKicks a player from the team");
            // /team promote
            MessageManager.sendMessage(sender, "&3/team promote (player) &7- &bPromote a player to manager");
            // /team demote
            MessageManager.sendMessage(sender, "&3/team demote (player) &7- &bDemote a player to member");
            // /team sethq
            MessageManager.sendMessage(sender, "&3/team sethq &7- &bSets the team's headquarters");
            // /team setrally
            MessageManager.sendMessage(sender, "&3/team setrally &7- &bSets the team's rally point");
        } else {
            try {
                TeamSubCommand tc = getSubCommand(args[0]);

                if (tc == null) {
                    MessageManager.sendMessage(sender, "&cUnrecognized team command!\nDo /team for help.");
                    return;
                }

                tc.execute(((Player) sender), fixArgs(args));
            } catch (Exception ex) {
                MessageManager.sendMessage(sender, "&cAn unexpected error occured: " + ex.getLocalizedMessage() + "\nContact an admin!");
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

                if (args[0].equalsIgnoreCase("j") || args[0].equalsIgnoreCase("join")) {
                    List<String> list2return = tm.getTeams().stream().filter(opt -> opt.getName().toLowerCase().startsWith(args[1])).map(Team::getName).collect(Collectors.toList());
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