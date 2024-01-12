# NotSoShadowExtras

## Info

Addon to [AntiShadowPatch](https://modrinth.com/mod/antishadowpatch) that adds extra(non-vanilla) features that won't be included in AntiShadowPatch.


## The list of features 
* Update Suppression Crash Fix **(SO & CCE)**
* Update Suppression Dupe Fix
* * Fixes some dupes related to update suppression. Item shadowing dupes aren't affected.
* Update Suppression Item Damage Fix
* * Fixes some issues with update suppression related to items not breaking properly.
* Update Suppression Hopper Dupe Fix
* * Fixes a hopper item dupe with updateSuppressionCrashFix enabled. It's a separate rule because of potential behavior changes.
* No Suppression Stacktrace
* * Completely hides update suppression stacktrace.
* Disable DataFixer Light Recalculation
* * With the changes Mojang made to the lightning engine, they added a datafixer that, when activated, forces Minecraft to recalculate light in every single chunk, which breaks EOL farms. This setting just removes the datafixer.


#### Commands (requires Fabric API)
* /nse swap <blockEntity> - modifies a block in the player's hand to include a block entity. Similar to block entity swap, but you can carry it around.


You can configure every setting individually in notsoshadowextras_config.json