package fi.dy.masa.tweakeroo.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.ShovelItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ShovelItem.class)
public interface IMixinShovelItem
{
    @Accessor("PATH_STATES")
    static Map<Block, Block> tweakeroo_getPathedBlocks() { return null; }
}
