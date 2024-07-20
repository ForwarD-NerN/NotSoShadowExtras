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
* Disable Light Recalculation DataFixer
* * When loading 1.19- chunks in 1.20+, the new light engine recalculates light in every single one of them. This option prevents it. If you have an EOL farm, you can load chunks with it once, and the EOL farm will work even without the mod enabled.
* Max Count Per Stack
* * Increases max stack size of the item. 
* * Note that while vanilla clients can see overstacked items and hold them in the inventory, they can't really move them and will also be kicked if they see a give command output. You can see this behavior [here](https://www.youtube.com/watch?v=8DBCKKCNCYo)


#### Commands (requires Fabric API)
* /nsse swap <blockEntity> - modifies a block in the player's hand to include a block entity. Similar to block entity swap, but you can carry it around.


You can configure every setting individually in notsoshadowextras_config.json