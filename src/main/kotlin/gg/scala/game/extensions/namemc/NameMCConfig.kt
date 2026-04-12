package gg.scala.game.extensions.namemc

/**
 * @date 2/7/2025
 */
data class NameMCConfig(
    var enabled: Boolean = true,
    var rewardCommand: MutableList<String> = mutableListOf(
        "chattagsadmin add %s Voter",
    ),
    val targetUrl: String = "https://api.namemc.com/server/%s/likes?profile=%s"
)
