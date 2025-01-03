<img style="text-align:center;" src="assets/example_banner.webp" alt="project banner">

---

<h1 style="text-align:center;">Maquillage</h1>

<p style="text-align:center;">
    <a href="https://github.com/Alathra/Template-Gradle-Plugin/blob/main/LICENSE">
        <img alt="GitHub License" src="https://img.shields.io/github/license/Alathra/Template-Gradle-Plugin?style=for-the-badge&color=blue&labelColor=141417">
    </a>
    <a href="https://github.com/Alathra/Template-Gradle-Plugin/releases">
        <img alt="GitHub Release" src="https://img.shields.io/github/v/release/Alathra/Template-Gradle-Plugin?include_prereleases&sort=semver&style=for-the-badge&label=LATEST%20VERSION&labelColor=141417">
    </a>
    <img alt="GitHub Actions Workflow Status" src="https://img.shields.io/github/actions/workflow/status/Alathra/Template-Gradle-Plugin/ci.yml?style=for-the-badge&labelColor=141417">
    <a href="https://github.com/Alathra/Template-Gradle-Plugin/issues">
        <img alt="GitHub Issues" src="https://img.shields.io/github/issues/Alathra/Template-Gradle-Plugin?style=for-the-badge&labelColor=141417">
    </a>
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/Alathra/Template-Gradle-Plugin?style=for-the-badge&labelColor=141417">
</p>

Maquillage is a plugin that lets you add tags and name colors that players can select through a GUI. You can add tags and name colors either through in-game commands or by importing them (see below).

Maquillage also supports nicknames, either through Maquillage itself, but it also has placeholders that support [EssentialsX](https://essentialsx.net/) nicknames.

To have Maquillage cosmetics appear in chat, you need a chat plugin that supports MiniMessage. Examples of this include [EssentialsX](https://essentialsx.net/) and [SunLight](https://www.spigotmc.org/resources/sunlight-%E2%AD%90-best-z-essentials-cmi-alternative.67733/). For Maquillage cosmetics in the tab-menu you need a tab-plugin that support MiniMessage, and an example of that is [TAB](https://www.spigotmc.org/resources/tab-1-5-1-21-1.57806/).

### Multi-server support
Maquillage can be run on multiple servers and will keep data up-to-date between them. To run on multiple servers, you need to use the same remote database for all servers. Any changes you make on one server will then be implemented on the other servers.

### Modules
Maquillage is entirely modular, and disabling any modules will not affect your ability to use the other modules.

### PlaceholderAPI
Maquillage uses PlaceholderAPI (PAPI) to provide parseable strings. These are the available placeholders:
```
%maquillage_namecolor% - The player's selected namecolor, followed by their username.
%maquillage_namecolor_nickname% - The player's selected namecolor, followed by their Maquillage nickname.
%maquillage_namecolor_essentialsnick% - The player's selected namecolor, followed by their EssentialsX nickname.
%maquillage_tag% - The player's selected tag, with a trailing white space.
%maquillage_tag_nospace% - The player's selected tag without a trailing white space.
```

---

## 🌟 Features

* Create tags and name colors with [MiniMessage](https://docs.advntr.dev/minimessage/index.html) support
* Player choosable tags and name colors, with permission support
* Bulk import tags and name colors
* Bulk import tags from other tag plugins
* Support for any chat or tab-menu plugin that supports MiniMessage

---

## 📦 Downloads (To be added)

<a href="https://github.com/Alathra/Template-Gradle-Plugin/releases/latest">
    <img alt="GitHub Downloads (all assets, all releases)" src="https://img.shields.io/github/downloads/Alathra/Template-Gradle-Plugin/total?style=for-the-badge&logo=github&logoColor=white&labelColor=141417">
</a>
<a href="https://www.spigotmc.org/">
    <img alt="Spiget Downloads" src="https://img.shields.io/spiget/downloads/9089?style=for-the-badge&logo=spigotmc&logoColor=white&label=SPIGOT&labelColor=141417">
</a>
<a href="https://modrinth.com/">
    <img alt="Modrinth Downloads" src="https://img.shields.io/modrinth/dt/essentialsx?style=for-the-badge&logo=modrinth&logoColor=white&label=MODRINTH&labelColor=141417">
</a>
<a href="https://hangar.papermc.io/">
    <img alt="Hangar Downloads" src="https://img.shields.io/hangar/dt/Essentials?style=for-the-badge&label=HANGAR&labelColor=141417">
</a>
<a href="https://www.curseforge.com/">
    <img alt="CurseForge Downloads" src="https://img.shields.io/curseforge/dt/93271?style=for-the-badge&logo=curseforge&logoColor=white&label=CurseForge&labelColor=141417">
</a>

### Stable Releases

Stable releases can be downloaded from the platforms linked above.

### Pre-Releases

Pre-releases/release-candidates are denoted by having `RC` in the name. These releases are made ahead of stable releases and should not be considered entirely stable.

### Experimental Builds

Experimental builds/snapshots are denoted by having `SNAPSHOT` in the name and should be considered unstable. These are bleeding edge builds produced from the latest available code. We do not recommend running these in production environments as these builds are unfinished and may contain serious issues.

---

## 🤝 Bugs & Feature Requests

If you happen to find any bugs or wish to request a feature, please open an issue on our [issue tracker here](https://github.com/Alathra/Maquillage/issues). We provide bug report and feature request templates, so it is important that you fill out all the necessary information.

Making your issue easy to read and follow will usually result in it being handled faster. Failure to provide the requested information in an issue may result in it being closed.

---

## 🚧 API

<a href="">
    <img alt="Documentation" src="https://img.shields.io/badge/DOCUMENTATION-900C3F?style=for-the-badge&labelColor=141417">
</a>
<a href="https://jitpack.io/com/github/Alathra/Template-Gradle-Plugin/latest/javadoc/">
    <img alt="Javadoc" src="https://img.shields.io/badge/JAVADOC-8A2BE2?style=for-the-badge&labelColor=141417">
</a>

We provide API for developers accessible through [JitPack](https://jitpack.io/). 

<details>
<summary>Gradle Kotlin DSL</summary>

```kotlin
repositories {
    maven("https://jitpack.io") {
        content {
            includeGroup("io.github.exampleuser")
        }
    }
}

dependencies {
    compileOnly("io.github.exampleuser:exampleplugin:VERSION")
}
```
</details>

<details>
<summary>Maven</summary>

```xml
<project>
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>io.github.exampleuser</groupId>
            <artifactId>exampleplugin</artifactId>
            <version>VERSION</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```
</details>

---

## 🔧 Contributing

Contributions are always welcome! Please make sure to read our [Contributor's Guide](CONTRIBUTING.md) for standards and our [Contributor License Agreement (CLA)](CONTRIBUTOR_LICENSE_AGREEMENT.md) before submitting any pull requests.

We also ask that you adhere to our [Contributor Code of Conduct](CODE_OF_CONDUCT.md) to ensure this community remains a place where all feel welcome to participate.

---

## 📝 Licensing

You can find the license the source code and all assets are under [here](../LICENSE). Additionally, contributors agree to the Contributor License Agreement \(*CLA*\) found [here](CONTRIBUTOR_LICENSE_AGREEMENT.md).

---

## ❤️ Acknowledgments

- **[darksaid98](https://github.com/darksaid98)** _for a wonderful plugin template and great advice and help during the development process_
