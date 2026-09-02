## Chat
To have Maquillage cosmetics appear in chat, you need a chat plugin that supports MiniMessage. Examples of this include [EssentialsX](https://essentialsx.net/) and [SunLight](https://www.spigotmc.org/resources/sunlight-%E2%AD%90-best-z-essentials-cmi-alternative.67733/). For Maquillage cosmetics in the tab-menu you need a tab-plugin that support MiniMessage, and an example of that is [TAB](https://www.spigotmc.org/resources/tab-1-5-1-21-1.57806/).

## Multi-server support
Maquillage can be run on multiple servers and will keep data up-to-date between them. To run on multiple servers, you need to use the same remote database for all servers. Any changes you make on one server will then be implemented on the other servers.

## Modules
Maquillage is entirely modular, and disabling any modules will not affect your ability to use the other modules.

## PlaceholderAPI
Maquillage uses PlaceholderAPI (PAPI) to provide parseable strings. These are the available placeholders:
```
%maquillage_namecolor% - The player's selected namecolor, followed by their username.
%maquillage_namecolor_nickname% - The player's selected namecolor, followed by their Maquillage nickname.
%maquillage_namecolor_essentialsnick% - The player's selected namecolor, followed by their EssentialsX nickname.
%maquillage_tag% - The player's selected tag, with a trailing white space.
%maquillage_tag_nospace% - The player's selected tag without a trailing white space.
```

## Adding cosmetics in-game
To get started adding cosmetics in-game, use the command `/maquillage create color` or `/maquillage create tag` and the chat conversation will guide you through the process. For bulk import of cosmetics, see below. You can also edit various aspects of the cosmetics with the commands `/maquillage edit color <variable> <identifier>` or `/maquillage edit tag <variable> <identifier>`.

## Cosmetic import examples
The following examples can be used in `data/import.yml` to bulk import cosmetics by running the command `"/maquillage import".

```yaml
tags: # Don't repeat this for new tags, simply make new sections under this key.
  example: # This text can be anything, as long as it's not duplicated in the same section. Using "example" under both tag and namecolor is fine, but "example" twice under tags is not.
    tag: "<dark_grey>[<green>Example<dark_grey>]" # This is what the tag will actually be in-game.
    label: "<grey>Example tag" # This is the label displayed in the GUI where players pick their tags.
    permission: "" # This can be anything you'd like. The final permission node (for tags) will be "maquillage.tag.<your input>". Leave blank for permissionless tag.
    weight: 0 # Higher weights get listed first in the GUI.
  another-example:
    tag: "<dark_grey>[<green>Another example<dark_grey>]"
    label: "<grey>Another example tag"
    permission: "anotherexample" # This will give the permission "maquillage.tag.anotherexample".
    weight: 100

namecolors: # Don't repeat this for new namecolors, simply make new sections under this key.
  example: # This text can be anything, as long as it's not duplicated in the same section. Using "example" under both tag and namecolor is fine, but "example" twice under tags is not.
    color: "<aqua>" # This is what the namecolor will actually be in-game.
    label: "<aqua>Example color" # This is the label displayed in the GUI where players pick their namecolors.
    permission: "" # This can be anything you'd like. The final permission node (for namecolors) will be "maquillage.namecolor.<your input>". Leave blank for permissionless namecolor.
    weight: 0
  another-example:
    color: "<green>"
    label: "<green>Another example color"
    permission: "green" # This will give the permission "maquillage.namecolor.green"
    weight: 100
```
