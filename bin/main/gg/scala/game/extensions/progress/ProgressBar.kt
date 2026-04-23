package gg.scala.game.extensions.progress

import java.util.*

class ProgressBar(
    private val steps: Int = 10,
    private var char: Char = '⬛',
    private var completedColor: String = "§a§l",
    private var uncompletedColor: String = "§e§l"
)
{
    fun build(percentage: Double): String
    {
        var percentage = percentage

        val steps = arrayOfNulls<String>(this.steps)
        Arrays.fill(steps, uncompletedColor + char)

        if (percentage > 100.0)
        {
            percentage = 100.0
        }

        val percentagePerStep = 100.0 / this.steps.toDouble()

        var i = 0
        while (i * percentagePerStep < percentage)
        {
            if (i > steps.size - 1)
            {
                break
            }

            if (i == steps.size - 1)
            {
                if (percentage < 100.0)
                {
                    break
                }
            }

            if (i > 0)
            {
                if (steps[i - 1]!!.startsWith(completedColor))
                {
                    steps[i++] = char.toString()
                } else
                {
                    steps[i++] = completedColor + char
                }
            } else
            {
                steps[i++] = completedColor + char
            }
        }

        return steps.joinToString(separator = "")
    }
}
