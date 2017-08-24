package lab9;

public class MyHashMapDriver {
	public static void main(String[] args) {
		Map61B<String, String> map = new MyHashMap<>();

		map.put("henry", "fruit cake");
		map.put("niko", "king of the world");
		map.put("mark", "poopie face");

		System.out.println(map.get("henry"));
		System.out.println(map.get("niko"));
		System.out.println(map.get("mark"));
		System.out.println(map.get("hi"));

	}
}