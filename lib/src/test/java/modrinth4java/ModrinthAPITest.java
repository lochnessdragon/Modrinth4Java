package modrinth4java;

import org.junit.Test;
import static org.junit.Assert.*;
import com.github.lochnessdragon.modrinth4java.ModrinthAPI;

public class ModrinthAPITest {
	@Test public void testGetProject() {
        ModrinthAPI.getProject("fabric-api");
        //assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }
}