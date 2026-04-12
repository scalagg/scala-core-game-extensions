package gg.scala.game.extensions.profile

import gg.scala.commons.persist.ProfileOrchestrator
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import java.util.*

@Service
object CorePlayerProfileService : ProfileOrchestrator<CorePlayerProfile>()
{
    override fun new(uniqueId: UUID) = CorePlayerProfile(uniqueId)
    override fun type() = CorePlayerProfile::class

    @Configure
    fun configure()
    {

    }
}
