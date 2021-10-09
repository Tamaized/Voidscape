package tamaized.voidscape.turmoil.skills;

import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.Voidscape;

public final class GenericSkillIcons {

	public static final ResourceLocation VOIDIC_BOND = assign("voidic_bond");
	public static final ResourceLocation SPELLPOWER = assign("spellpower");
	public static final ResourceLocation VOIDIC_BONDING = assign("voidic_bonding");
	public static final ResourceLocation SPELL_DEX = assign("spell_dex");
	public static final ResourceLocation ASTUTE_UNDERSTANDING = assign("astute_understanding");
	public static final ResourceLocation VOIDIC_DAMAGE = assign("voidic_damage");
	public static final ResourceLocation VOIDIC_HAMP = assign("voidic_hamp");
	public static final ResourceLocation VOIDIC_DEFENSE = assign("voidic_defense");

	private static ResourceLocation assign(String name) {
		return new ResourceLocation(Voidscape.MODID, "textures/skills/" + name + ".png");
	}

	private GenericSkillIcons() {

	}

}
