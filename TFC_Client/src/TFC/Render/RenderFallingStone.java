package TFC.Render;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;

import TFC.*;
import TFC.Entities.EntityFallingStone;

public class RenderFallingStone extends Render
{
    private RenderBlocks renderBlocks;

    
    public RenderFallingStone()
    {
        renderBlocks = new RenderBlocks();
        shadowSize = 0.5F;

    }

    public void doRenderFallingStone(EntityFallingStone entityfallingstone, double d, double d1, double d2,
            float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        loadTexture("/bioxx/terraRock.png");

        World world = entityfallingstone.getWorld();
        GL11.glDisable(2896 /*GL_LIGHTING*/);

        TFC_CoreRender.renderBlockFallingSand(Block.blocksList[entityfallingstone.blockID], entityfallingstone.metaID, world, MathHelper.floor_double(entityfallingstone.posX), MathHelper.floor_double(entityfallingstone.posY), MathHelper.floor_double(entityfallingstone.posZ));

        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        doRenderFallingStone((EntityFallingStone)entity, d, d1, d2, f, f1);
    }
}
