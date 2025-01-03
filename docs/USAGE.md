## Cosmetic import examples
The following examples can be used in `import.yml` to bulk import cosmetics by running the command `"/maquillage import".

```yaml
tags: # Don't repeat this for new tags, simply make new sections under this key.
    example: # This text can be anything, as long as it's not duplicated in the same section. Using "example" under both tag and namecolor is fine, but "example" twice under tags is not.
        string: "<dark_grey>[<green>Example<dark_grey>]" # This is what the tag will actually be in-game.
        label: "<grey>Example tag" # This is the label displayed in the GUI where players pick their tags.
        permission-node: "" # This can be anything you'd like. The final permission node (for tags) will be "maquillage.tag.<your input>". Leave blank for permissionless tag.
    another-example:
        string: "<dark_grey>[<green>Another example<dark_grey>]"
        label: "<grey>Another example tag"
        permission-node: "anotherexample" # This will give the permission "maquillage.tag.anotherexample".

namecolors: # Don't repeat this for new namecolors, simply make new sections under this key.
    example: # This text can be anything, as long as it's not duplicated in the same section. Using "example" under both tag and namecolor is fine, but "example" twice under tags is not.
        string: "<aqua>" # This is what the namecolor will actually be in-game.
        label: "<aqua>Example color" # This is the label displayed in the GUI where players pick their namecolors.
        permission-node: "" # This can be anything you'd like. The final permission node (for namecolors) will be "maquillage.namecolor.<your input>". Leave blank for permissionless namecolor.
    another-example:
        string: "<green>"
        label: "<green>Another example color"
        permission-node: "green" # This will give the permission "maquillage.namecolor.green"
```
