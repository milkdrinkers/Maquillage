<h1 align="center">Maquillage</h1>
<p align="center">
    <img src="https://img.shields.io/github/license/Alathra/Template-Gradle-Plugin?color=blue&style=flat-square" alt="license"/>
    <img alt="GitHub release (latest by SemVer including pre-releases)" src="https://img.shields.io/github/downloads-pre/Alathra/Template-Gradle-Plugin/latest/total?style=flat-square">
    <img alt="GitHub release" src="https://img.shields.io/github/downloads-pre/Alathra/Template-Gradle-Plugin/latest?style=flat-square">
    <img alt="GitHub Workflow Status (with event)" src="https://img.shields.io/github/actions/workflow/status/Alathra/Template-Gradle-Plugin/release.yml?style=flat-square">
    <img alt="GitHub issues" src="https://img.shields.io/github/issues/Alathra/Template-Gradle-Plugin?style=flat-square">
</p>

---

## Description

Coming soon (tm)

## Cosmetic import examples
The following examples can be used in `import.yml` to bulk-import cosmetics by running the command `"/maquillage import"`.

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


## Statistics

<img src="https://bstats.org/signatures/bukkit/maquillage.svg"/>

---
