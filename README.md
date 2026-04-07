# AntiAutoPlace [![Spigot Version](https://img.shields.io/badge/Spigot-1.8.8-orange.svg)](https://www.spigotmc.org/)
A plugin that detects players using autoplace

---

## Features

- Detection of block placement cheats.
- Alert system for moderators with customizable messages.
- Auto punishment system.
- Supports integration with PlaceholderAPI for advanced message formatting.
- Easy plugin configuration and setup.

---

## Permissions

| Permission                  | Description                                                      |
|------------------------------|------------------------------------------------------------------|
| `antiautoplace.bypass`       | Allows players to bypass the block place detection checks.        |
| `antiautoplace.alerts`       | Grants players permission to receive alert notifications.        |

---

## Installation

1. Download the latest version of the plugin from the [releases page](https://github.com/Jarlyt/AntiAutoPlace/releases)
2. Place the `.jar` file into your server's `plugins` folder.
3. Restart or reload your server.
4. Configure the plugin via the `config.yml` file (created on first run).

---

## Configuration

After first launch, edit the `config.yml` to customize settings

```yaml
# Controls whether detected actions are canceled automatically
cancel: true 

# Alert settings for moderators
alerts:
  enabled: true  # Enable or disable alerts when a player is flagged
  message: "&7[&eAntiAutoPlace&7] &c%player% is using autoplace!"  # Message sent to moderators; %player% will be replaced with the player's name

# Punishment configurations
punishment:
  enabled: true  # Enable or disable automatic punishments
  command: "ban-ip %player% 7d autoplace"  # Command executed when a player is detected; %player% will be replaced with the player's name
```
## Issues
If you encounter any issues with the plugin:

- Open a new issue in the [repository's issues section](https://github.com/Jarlyt/AntiAutoPlace/issues) and include details:
  - Description of the issue
  - Any relevant logs or errors

---

**Your feedback and bug reports help us improve the plugin!**