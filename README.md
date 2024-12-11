# ItemCreator
Items Creation Library for Minecraft Plugins
***

**Note**: This library requires PaperMC server software in order to work. Make sure that your plugin uses the papermc-api instead of the usual (and obsulate) spigot-api


## How to get the library
You can include this library in your project by declaring the following dependency:

Maven:
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.Asintotoo</groupId>
    <artifactId>ItemCreator</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

Gradle:
```groovy
repositories {
  mavenCentral()
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.Asintotoo:ItemCreator:VERSION'
}
```
Replace VERSION with the latest version avaiable that can be found [here](https://github.com/Asintotoo/ItemCreator/releases/latest)

## How to use
You can either create an Item from scratch or describe it in a .yml file as following:
```java
Player player = ... // Your method to get a Player instance
ItemStack myItem = ItemCreator.of(Material.POTATO) // Create an item based on a given Material
                                 .display_name("&eRandom Potato of %player_name%") // Set the name of the item using color codes
                                 .amount(5) // Set the amount
                                 .source(player) // Set the item source from the given player, which means that any placeholders will be parsed as the given player (Requires PlaceholdersAPI installed on the server)
                                 .build() // Build the item 
                                 .itemstack(); // Return the ItemStack version

ItemStack myItemFromConfig = ItemCreator.of(plugin.getConfig(), "items.king-sword") // This will create an item by reading from a YamlConfiguration file at the given path
                                        .build()
                                        .itemstack();
```
Here is the  example of the YamlConfiguration format used above:
```yaml
items:
  king-sword:
    material: DIAMOND_SWORD # Specify the Material of the item
    display-name: "&dKing's Sword" # Set its display name
    custom-model-data: 2 # Set a Custom Model Data
    enchantments: # Specify the list of the item's enchants
      - "sharpness:5"
      - "looting:3"
    unbreakable: true # Make the item unbreakable
```

It's also possible to get an ItemProvider from an ItemsAdder's or an Oraxen's custom item, this requires ItemsAdder or Oraxen to be installed on the server:
```java
ItemStack myItemsAdderItem = ItemCreator.ofItemsAdder("my-namespace:my-item-id") // Get the ItemsAdder item at the given namespace id as an ItemProvider
                                        .build() // Build the ItemProvider
                                        .itemstack(); // Convert it to ItemStack

ItemStack myOraxenItem = ItemCreator.ofOraxen("my-oraxen-item-id") // Get the Oraxen item with the given id as an ItemProvider
                                        .build() // Build the ItemProvider
                                        .itemstack(); // Convert it to ItemStack
```

An improved documentation will come soon...
