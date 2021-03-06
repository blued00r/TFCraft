package TFC.Render;

import org.lwjgl.opengl.GL11;

import TFC.Entities.Mobs.EntityCowTFC;
import TFC.Entities.Mobs.EntityPigTFC;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;

public class RenderCowTFC extends RenderLivingTFC
{
    public RenderCowTFC(ModelBase par1ModelBase, float par2)
    {
        super((ModelBaseTFC)par1ModelBase, par2);
    }

    public void renderCow(EntityCowTFC par1EntityCow, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRenderLiving(par1EntityCow, par2, par4, par6, par8, par9);
    }
    
    protected void preRenderCallback (EntityLiving par1EntityLiving, float par2)
    {
	preRenderScale ((EntityCowTFC) par1EntityLiving, par2);
    }

    protected void preRenderScale (EntityCowTFC par1EntityCow, float par2)
    {
	GL11.glScalef (par1EntityCow.size_mod,par1EntityCow.size_mod,par1EntityCow.size_mod);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderCow((EntityCowTFC)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderCow((EntityCowTFC)par1Entity, par2, par4, par6, par8, par9);
    }
}
