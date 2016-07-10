package com.mccritz.kmain.teams.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

public abstract class TeamSubCommand {

    String name;
    boolean manager;
    boolean leaderOnly;
    List<String> aliases;

    public TeamSubCommand(String name) {
	this(name, false, new LinkedList<String>(), false);
    }

    public TeamSubCommand(String name, List<String> aliases) {
	this(name, false, aliases, false);
    }

    public TeamSubCommand(String name, boolean manager, List<String> aliases, boolean leaderOnly) {
	this.name = name;
	this.manager = manager;
	this.aliases = aliases;
	this.leaderOnly = leaderOnly;
    }

    public TeamSubCommand(String name, boolean leaderOnly, List<String> aliases) {
	this.name = name;
	this.leaderOnly = leaderOnly;
	this.manager = false;
	this.aliases = aliases;
    }

    public String getName() {
	return name;
    }

    public boolean isManager() {
	return manager;
    }

    public List<String> getAliases() {
	return aliases;
    }

    public abstract void execute(Player p, String[] args);
}