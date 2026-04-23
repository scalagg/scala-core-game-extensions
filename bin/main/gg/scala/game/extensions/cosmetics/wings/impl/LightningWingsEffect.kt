//package gg.scala.game.extensions.cosmetics.wings.impl
//
//import com.cryptomorin.xseries.XMaterial
//import gg.scala.flavor.service.Configure
//import gg.scala.flavor.service.Service
//import gg.scala.game.extensions.cosmetics.CosmeticRegistry
//import gg.scala.game.extensions.cosmetics.ImmutableResource
//import gg.scala.game.extensions.cosmetics.wings.WingCosmeticCategory
//import gg.scala.game.extensions.cosmetics.wings.WingParticleEffects
//import net.evilblock.cubed.util.bukkit.ItemBuilder
//import org.bukkit.Color
//
///**
// * @author Pawful
// * @since 06/01/2025
// */
//@Service
//object LightningWingsEffect : WingParticleEffects
//{
//    @Configure
//    fun configure()
//    {
//        CosmeticRegistry.register(
//            WingCosmeticCategory,
//            this
//        )
//    }
//
//    override val primary: Color = Color.BLUE
//    override val secondary: Color = Color.PURPLE
//    override val tertiary: Color = Color.WHITE
//
//    override val name = "Lightning"
//    override val description = listOf(
//        "Harness the power of lightning",
//        "with electric wings!"
//    )
//    override val icon = ItemBuilder
//        .of(XMaterial.END_ROD)
//
//    override val resource = ImmutableResource("wings", "lightning")
//    override val price = 35_000.0f
//}