package de.abbaddie.wot.fleet.data.ovent;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ImmutableMap;

import de.abbaddie.wot.data.coordinates.StaticCoordinates;
import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.data.spec.Specs;
import de.abbaddie.wot.fleet.data.ovent.FleetOventNature.FleetOventFleet;

// not working
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = "/applicationContext-test.xml")
public class FleetOventNatureTest {
	@Test
	public void testUnserialize() {
		FleetOventNature nature = new FleetOventNature();
		String str = "aa:1:{i:0;a:14:{s:7:\"ownerID\";s:4:\"6951\";s:8:\"ofiaraID\";N;s:13:\"startPlanetID\";s:7:\"1456287\";s:14:\"targetPlanetID\";N;s:9:\"resources\";a:3:{s:5:\"metal\";s:8:\"27573462\";s:7:\"crystal\";s:7:\"7542778\";s:9:\"deuterium\";s:8:\"26718173\";}s:11:\"startCoords\";a:4:{i:0;s:1:\"1\";i:1;s:3:\"289\";i:2;s:1:\"5\";i:3;s:1:\"3\";}s:12:\"targetCoords\";a:4:{i:0;s:1:\"1\";i:1;s:3:\"117\";i:2;s:2:\"13\";i:3;N;}s:4:\"spec\";a:5:{i:203;s:4:\"6880\";i:208;s:1:\"2\";i:209;s:1:\"1\";i:210;s:2:\"20\";i:214;s:1:\"2\";}s:8:\"cssClass\";s:8:\"colonize\";s:9:\"missionID\";s:1:\"9\";s:15:\"startPlanetName\";s:4:\"Mond\";s:16:\"targetPlanetName\";N;s:7:\"fleetID\";s:8:\"10932715\";s:7:\"passage\";s:6:\"return\";}}";
		
		nature.unserialize(str);
		
		assertEquals(1, nature.getFleets().size());
		
		Map<Integer, Long> levels = ImmutableMap.of(203, 6880L, 208, 2L, 209, 1L, 210, 2L, 214, 2L);
		SpecSet<?> specs = Specs.getStaticSpecSet(Specs.convertToHardByLong(levels));
		
		FleetOventFleet fleet = nature.getFleets().get(0);
		assertEquals(6951, fleet.getOwnerId());
		assertEquals(0, fleet.getOfiaraId());
		assertEquals(1456287, fleet.getStartPlanetId());
		assertEquals(0, fleet.getTargetPlanetId());
		assertEquals(27573462d, fleet.getResources().getValue("metal"), 1e-15);
		assertEquals(7542778d, fleet.getResources().getValue("crystal"), 1e-15);
		assertEquals(26718173d, fleet.getResources().getValue("deuterium"), 1e-15);
		assertEquals(new StaticCoordinates(1, 289, 5, 3), fleet.getStartCoords());
		assertEquals(new StaticCoordinates(1, 117, 13, 0), fleet.getTargetCoords());
		assertEquals(specs, fleet.getSpecs());
		assertEquals("colonize", fleet.getCssClass());
		assertEquals(9, fleet.getMissionId());
		assertEquals("Mond", fleet.getStartPlanetName());
		assertEquals(null, fleet.getTargetPlanetName());
		assertEquals(10932715, fleet.getFleetId());
		assertEquals("return", fleet.getPassage());
	}
}
