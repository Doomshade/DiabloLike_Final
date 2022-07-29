package cz.helheim.rpg;


import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Jakub Šmrha
 * @since 27.07.2022
 */
class DiabloLikeImplTest {
	private static DiabloLike plugin;
	private static ServerMock serverMock;

	@BeforeAll
	static void setUp() {
		serverMock = MockBukkit.mock();
		final PlayerMock playerMock = serverMock.addPlayer();
		playerMock.setDisplayName("ASDASDS");
	}


	@AfterAll
	public static void tearDown() {
	}

	@Test
	public void testMethod() {
	}
}
