package example;

import com.github.lochnessdragon.modrinth4java.ModrinthAPI;
import com.github.lochnessdragon.modrinth4java.Project;

public class Example {

    public static void main(String[] args) {
        System.out.println("Example modrinth app");
        Project fabricApi = ModrinthAPI.getProject("fabric-api");
		System.out.println("Recovered Project Details:");
		System.out.println(fabricApi);
    }
}