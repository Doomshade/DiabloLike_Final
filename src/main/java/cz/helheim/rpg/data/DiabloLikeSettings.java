package cz.helheim.rpg.data;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.api.io.Settings;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public class DiabloLikeSettings extends Settings {

	//<editor-fold desc="Private variables">
	//<editor-fold desc="Tier colours">
	private final Map<DiabloItem.Tier, String> tierColours = new HashMap<>(DiabloItem.Tier.values().length);
	//</editor-fold>
	//<editor-fold desc="Gem chances">
	private final Map<DiabloItem.Tier, Integer> gemChances = new HashMap<>(DiabloItem.Tier.values().length);
	//</editor-fold>
	//<editor-fold desc="Attribute tiers">
	private final Map<DiabloItem.Tier, Double> attributeTiers = new HashMap<>(DiabloItem.Tier.values().length);
	//</editor-fold>
	//<editor-fold desc="Obrana multipliers">
	private final Map<String, Double> obranaMultipliers = new HashMap<>(6);
	//</editor-fold>
	//<editor-fold desc="Custom rarities">
	private final Map<DiabloItem.Tier, List<String>> customRarities = new HashMap<>(DiabloItem.Tier.values().length);
	//</editor-fold>
	//<editor-fold desc="Rarity Chances">
	private final Map<DiabloItem.Tier, Double> rarityChances = new HashMap<>(DiabloItem.Tier.values().length);
	//</editor-fold>
	//<editor-fold desc="Root entries">
	private String requiredLevelFormat;
	private String unidentifiedItemLore;
	private double dropChance;
	private int obrana;
	private String requirementSeparator;
	//</editor-fold>
	//<editor-fold desc="Drop range">
	private Range dropRange;
	//</editor-fold>
	//<editor-fold desc="Identifier">
	private String identifierNamePattern;
	private String identifierLorePattern;
	//</editor-fold>

	private int priceCap;
	//</editor-fold>

	private ConfigurationSection currentSection;

	public DiabloLikeSettings(final HelheimPlugin plugin) {
		super(plugin);
		currentSection = super.config;
	}

	public void reload() {
		loadRootEntries();
		loadTierColours();
		loadObranaMultipliers();
		loadCustomRarities();
		loadDropRange();
		loadRarityChances();
		loadIdentifier();
		loadAttributeTiers();
		loadGemChances();
		loadPriceConfiguration();
	}

	//<editor-fold desc="Config section accessors">
	private String _string(String section) {
		String s = currentSection.getString(section, "");
		if (s.isEmpty()) {
			logger.log(Level.INFO, "Empty string section: " + section);
			return s;
		}
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	private int _int(String section) {
		return currentSection.getInt(section, 0);
	}

	private double _double(String section) {
		return currentSection.getDouble(section, 0d);
	}

	private List<String> _list(String section) {
		List<String> list = currentSection.getStringList(section);
		if (list == null || list.isEmpty()) {
			logger.log(Level.INFO, "Empty string list section: " + section);
			return new ArrayList<>(0);
		}
		return list.stream()
		           .map(x -> x.isEmpty() ? x : ChatColor.translateAlternateColorCodes('&', x))
		           .collect(Collectors.toList());
	}
	//</editor-fold>

	//<editor-fold desc="Root entries">
	private void loadRootEntries() {
		currentSection = config;
		requiredLevelFormat = _string("required-level-lore");
		unidentifiedItemLore = _string("unidentified-item-lore");
		dropChance = _double("drop-chance");
		obrana = _int("obrana");
		requirementSeparator = _string("requirement-separator");
	}

	public int getObrana() {
		return obrana;
	}

	public String getRequirementSeparator() {
		return requirementSeparator;
	}

	public <T> String getRequiredLevelFormat(T level) {
		return requiredLevelFormat.replace("<lvl>", level.toString());
	}

	public String getUnidentifiedItemLore() {
		return unidentifiedItemLore;
	}

	public double getDropChance() {
		return dropChance;
	}

	//</editor-fold>

	//<editor-fold desc="Tier Colour">
	private void loadTierColours() {
		currentSection = config.getConfigurationSection("tier-colors");
		for (DiabloItem.Tier tier : DiabloItem.Tier.values()) {
			final String tierName = tier.name()
			                            .toLowerCase();
			tierColours.put(tier, _string(tierName));
		}
	}

	public String getTierColour(DiabloItem.Tier tier) {
		return tierColours.get(tier);
	}
	//</editor-fold>

	//<editor-fold desc="Obrana multiplier">
	private void loadObranaMultipliers() {
		currentSection = config.getConfigurationSection("obrana-multiplier");
		currentSection.getValues(false)
		              .forEach((x, y) -> obranaMultipliers.put(x, (double) y));
	}

	public double getObranaMultiplier(String armor) {
		return obranaMultipliers.getOrDefault(armor, 0d);
	}
	//</editor-fold>

	//<editor-fold desc="Custom rarities">
	private void loadCustomRarities() {
		currentSection = config.getConfigurationSection("custom-rarities");
		for (DiabloItem.Tier tier : DiabloItem.Tier.values()) {
			final String tierName = tier.name()
			                            .toLowerCase();
			customRarities.put(tier, _list(tierName));
		}
	}

	public List<String> getCustomRarity(DiabloItem.Tier tier) {
		return customRarities.get(tier);
	}


	//</editor-fold>

	//<editor-fold desc="Drop range">
	private void loadDropRange() {
		currentSection = config.getConfigurationSection("range");
		int min = _int("min-level");
		int max = _int("max-level");
		dropRange = new Range(min, max);
	}

	public Range getDropRange() {
		return dropRange;
	}
	//</editor-fold>

	//<editor-fold desc="Rarity chances">
	private void loadRarityChances() {
		currentSection = config.getConfigurationSection("chance");
		for (DiabloItem.Tier tier : DiabloItem.Tier.values()) {
			final String tierName = tier.name()
			                            .toLowerCase();
			rarityChances.put(tier, _double(tierName));
		}
	}


	public Map<DiabloItem.Tier, Double> getRarityChances() {
		return rarityChances;
	}

	public double getRarityChance(DiabloItem.Tier tier) {
		return rarityChances.get(tier);
	}
	//</editor-fold>

	//<editor-fold desc="Identifier">
	private void loadIdentifier() {
		currentSection = config.getConfigurationSection("item");
		identifierNamePattern = _string("displayName");
		identifierLorePattern = _string("lore").replaceAll("<lvl_max>", "(?<lvl>\\d+)");
	}

	public String getIdentifierNamePattern() {
		return identifierNamePattern;
	}

	public <T> String getIdentifierLorePattern(T levelMin, T levelMax) {
		return identifierLorePattern.replace("<lvl_max>", levelMax.toString())
		                            .replace("<lvl_min>", levelMin.toString());
	}
	//</editor-fold>

	//<editor-fold desc="Attribute tiers">
	private void loadAttributeTiers() {
		currentSection = config.getConfigurationSection("pocetDiv");
		for (DiabloItem.Tier tier : DiabloItem.Tier.values()) {
			final String tierName = tier.name()
			                            .toLowerCase();
			attributeTiers.put(tier, _double(tierName));
		}
	}

	public double getPocetDiv(DiabloItem.Tier tier) {
		return attributeTiers.get(tier);
	}
	//</editor-fold>

	//<editor-fold desc="Gem chances">
	private void loadGemChances() {
		currentSection = config.getConfigurationSection("gem-chance");
		for (DiabloItem.Tier tier : DiabloItem.Tier.values()) {
			final String tierName = tier.name()
			                            .toLowerCase();
			gemChances.put(tier, _int(tierName));
		}
	}

	public int getGemChance(DiabloItem.Tier tier) {
		return gemChances.get(tier);
	}
	//</editor-fold>

	private void loadPriceConfiguration() {
		currentSection = config.getConfigurationSection("price-configuration");

	}
}
