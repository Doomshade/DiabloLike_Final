package cz.helheim.rpg.diablolike.item;

import cz.helheim.rpg.diablolike.api.DiabloLike;
import cz.helheim.rpg.diablolike.data.DiabloLikeSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 27.07.2022
 */
public class LoreParser {
	public static final Pattern LA_PATTERN = Pattern.compile("\\+(?<range>(?<upper>\\d+)(-(?<lower>\\d+))?)%? .+");
	public static final Pattern SAPI_PATTERN = Pattern.compile(".+: (?<range>(?<upper>\\d+)(-(?<lower>\\d+))?)");
	private final DiabloLikeSettings settings = DiabloLike.getInstance()
	                                                      .getSettings();

	private final List<String> lore = new ArrayList<>();

	public LoreParser(final List<String> lore) {
		this.lore.addAll(lore);
	}

	public List<String> getAttributes() {
		final String requirementSeparator = settings.getRequirementSeparator();
		final int index = lore.indexOf(requirementSeparator);
		return lore.stream()
		           .filter(s -> LA_PATTERN.matcher(s)
		                                  .find() || SAPI_PATTERN.matcher(s)
		                                                         .find())
		           .collect(Collectors.toList());
	}

	public List<String> getRequirements() {
		final String requirementSeparator = settings.getRequirementSeparator();
		final int index = lore.indexOf(requirementSeparator);
		return index < 0 ? lore : lore.subList(index, lore.lastIndexOf(requirementSeparator) + 1);
	}

	public int findItemLevel() {
		final Pattern lvlPattern = Pattern.compile(settings.getRequiredLevelFormat());
		for (final String s : lore) {
			final Matcher m = lvlPattern.matcher(s);
			if (m.find()) {
				return Integer.parseInt(m.group("lvl"));
			}
		}
		return -1;
	}
}
