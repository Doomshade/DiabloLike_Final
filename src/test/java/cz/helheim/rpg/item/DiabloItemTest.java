package cz.helheim.rpg.item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.helheim.rpg.item.ItemInstantiationHelper.diabloItem;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.mockito.Mockito.*;

/**
 * @author Jakub Šmrha
 * @since 27.07.2022
 */
class DiabloItemTest {
	private static ItemStack itemStack;
	private DiabloItem diabloItem;

	@BeforeAll
	static void beforeAll() {
		itemStack = mock(ItemStack.class);
		final ItemMeta meta = mock(ItemMeta.class);
		// displayName
		doAnswer(answer -> when(meta.getDisplayName()).thenReturn(answer.getArgument(0, String.class))).when(meta)
		                                                                                               .setDisplayName(any());
		when(meta.hasDisplayName()).thenReturn(true);
		// lore
		doAnswer(answer -> when(meta.getLore()).thenReturn((List<String>) answer.getArgument(0, List.class))).when(meta)
		                                                                                                     .setLore(any());

		doAnswer(answer ->
		         {
			         final ItemMeta arg = answer.getArgument(0);
			         when(itemStack.getItemMeta()).thenReturn(arg);
			         return true;
		         }).when(itemStack)
		           .setItemMeta(any());
		when(itemStack.hasItemMeta()).thenReturn(true);
		when(itemStack.clone()).thenCallRealMethod();
		meta.setDisplayName(ChatColor.GREEN + "Test");
		meta.setLore(Stream.of("&4Síla: 6-11",
		                       "&9Inteligence: 4-8",
		                       "&cVitalita: 5-6",
		                       "&aPřesnost: 4-8",
		                       "",
		                       "&8----------------------",
		                       "&4Pouze pro: &aLučištník",
		                       "&2Potřebný Lvl: 32",
		                       "&8----------------------")
		                   .map(s -> s.isEmpty() ? s : ChatColor.translateAlternateColorCodes('&', s))
		                   .collect(Collectors.toList()));
		System.out.println(meta.getDisplayName());
		System.out.println(meta.getLore());
		itemStack.setItemMeta(meta);
	}

	@BeforeEach
	void setUp() {
		final Random random = new Random();
		final Map<DiabloItem.Tier, Double> customRarities = new HashMap<DiabloItem.Tier, Double>() {
			{
				for (DiabloItem.Tier tier : DiabloItem.Tier.values()) {
					put(tier, random.nextDouble() * 100d);
				}
			}
		};
		diabloItem =
				diabloItem("test", itemStack, 32, random.nextDouble() * 100, customRarities, random.nextBoolean(), random.nextBoolean());
	}

	@Test
	public void originalLoreTest() {
		final List<String> originalLore = diabloItem.getOriginalLore();
		assertLinesMatch(itemStack.getItemMeta()
		                          .getLore(), originalLore);
	}

	@Test
	public void setIdentifiedTest() {
		List<String> attributes = Stream.of("&4Síla: 8",
		                                    "&9Inteligence: 6",
		                                    "&cVitalita: 5",
		                                    "&aPřesnost: 7",
		                                    "",
		                                    "&8----------------------",
		                                    "&4Pouze pro: &aLučištník",
		                                    "&2Potřebný Lvl: 32",
		                                    "&8----------------------")
		                                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
		                                .collect(Collectors.toList());
		diabloItem.setIdentified(attributes, true);
		assertLinesMatch(diabloItem.getItemStack()
		                           .getItemMeta()
		                           .getLore(), attributes);
	}
}
