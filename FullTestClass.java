// Test Sokoban with junit

import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FullTestClass {
	@Test
	public void existenceTest() {
		System.out.println("Sokoban class existence test: ");
		Sokoban sokoban = new Sokoban();
//		check if the sokoban object is not null
		assertNotNull(sokoban);
		System.out.println("\tPassed");
	}

//	test for readValidInput
	@Test
	public void readValidInputTest01(){
		System.out.println("readValidInput() method test map: ");
		readValidInputTest(new String[]{ "W", "A", "S", "D"}, new String[]{ "w", "a", "s", "d",});
	}

	@Test
	public void readValidInputTest02(){
		System.out.println("readValidInput() method test map: ");
		readValidInputTest(new String[]{ "q", "r", "h"}, new String[]{ "Q", "R", "H", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"0", "!", "@", "#", "$", "%", "^", "&", "*"});
	}

	@Test
	public void readValidInputTest03(){
		System.out.println("readValidInput() method test 02: ");
		readValidInputTest(new String[]{"Sokoban"}, new String[]{ "(", ")", "_", "-", "+", "=", "{", "}", "[", "]", "|",
				"\\", ":", ";", "", "\"", "<", ">", ",", ".", "?", "/", "`", "~", " ", "sokoban"});
	}

	private void readValidInputTest(String[] testCases, String[] negativeTestCases){
		String[] allCases = Arrays.copyOf(testCases, testCases.length + negativeTestCases.length);
		System.arraycopy(negativeTestCases, 0, allCases, testCases.length, negativeTestCases.length);

		HashMap<String, Boolean> results = new HashMap<>();
		Thread[] inputThreads = new Thread[allCases.length];

		// save the original output streams
		PrintStream originalOut = System.out;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));

		for (int i = 0; i < allCases.length; i++) {
			final int index = i;
			inputThreads[i] = new Thread(() -> {
				try {
					InputStream inputStream = System.in;
					System.setIn(new ByteArrayInputStream(allCases[index].getBytes()));

					Sokoban sokoban = new Sokoban();
					char cleanedChar = sokoban.readValidInput();
					results.put(allCases[index], cleanedChar == allCases[index].charAt(0));
					System.setIn(inputStream);

				} catch (Exception e) {}
			});
		}

		for (int i = 0; i < allCases.length; i++) {
			try {
				inputThreads[i].start();
				inputThreads[i].join(50);
			} catch (InterruptedException e) {}
		}
		System.setOut(originalOut);

		for (int i = 0; i < testCases.length; i++) {
			if(!results.containsKey(testCases[i])){
				fail("InputTest failed for " + testCases[i]);
			}
			assertTrue(results.containsKey(testCases[i]));
		}

		System.out.println("\tPassed for positive test cases");

		for (int i = 0; i < negativeTestCases.length; i++) {
			if(results.containsKey(negativeTestCases[i])){
				fail("InputTest failed for " + negativeTestCases[i]);
			}
			assertFalse(results.containsKey(negativeTestCases[i]));
		}

		System.out.println("\tPassed for negative test cases");
	}

	@Test
	public void fixMapTest01(){
		System.out.println("fixMap() method map test: ");
		char[][] testMap = readMap("./testMaps/fixMap/ori");
		char[][] alteredMap = readMap("./testMaps/fixMap/altered/01");
		char[][] fixedMap = readMap("./testMaps/fixMap/fixed/01");
		fixMapTest(testMap, alteredMap, fixedMap);
	}

	@Test
	public void fixMapTest02(){
		System.out.println("fixMap() method map test: ");
		char[][] testMap = readMap("./testMaps/fixMap/ori");
		char[][] alteredMap = readMap("./testMaps/fixMap/altered/02");
		char[][] fixedMap = readMap("./testMaps/fixMap/fixed/02");
		fixMapTest(testMap, alteredMap, fixedMap);
	}

	private int numberOfRows(String fileName) {
		// read the file
		try (Scanner sc = new Scanner(new File(fileName))) {
			int count = 0;
			while (sc.hasNextLine()) {
				sc.nextLine();
				count++;
			}
			return count;
		} catch (Exception e) {
			return -1;
		}
	}
	private char[][] readMap(String fileName) {
		try (Scanner sc = new Scanner(new File(fileName))) {
			int row = numberOfRows(fileName);
			char[][] map = new char[row][];
			for (int i = 0; i < row; i++) {
				map[i] = sc.nextLine().toCharArray();
			}
			return map;
		} catch (Exception e) {
			return null;
		}
	}

	private void fixMapTest(char[][] testMap, char[][] alteredMap, char[][] fixedMap){
		Sokoban sokoban = new Sokoban();
		sokoban.fixMap(alteredMap, testMap);
		assertTrue(Arrays.deepEquals(fixedMap, alteredMap));
		System.out.println("\tPassed");
	}

	@Test
	public void moveBoxTest01(){
		System.out.println("moveBox() method map test: ");
		moveBoxTest("./testMaps/moveBox/01", 4, 3);
	}

	@Test
	public void moveBoxTest02(){
		System.out.println("moveBox() method map test: ");
		moveBoxTest("./testMaps/moveBox/02", 1, 1);
	}

	@Test
	public void moveBoxTest03(){
		System.out.println("moveBox() method 02 test: ");
		moveBoxTest("./testMaps/moveBox/03", 6, 5);
	}

	@Test
	public void moveBoxTest04(){
		System.out.println("moveBox() method 04 test: ");
		moveBoxTest("./testMaps/moveBox/04", 3, 3);
	}

	private void moveBoxTest(String path, int row, int col){
		Sokoban sokoban = new Sokoban();

		char[][] testMapOri = readMap(path + "/ori");
		char[][] resultMapD = readMap(path + "/D");
		char[][] resultMapW = readMap(path + "/W");
		char[][] resultMapS = readMap(path + "/S");
		char[][] resultMapA = readMap(path + "/A");

		char[][] testMap;
		if(resultMapD != null){
			testMap = CopyMap(testMapOri);
			sokoban.moveBox(testMap, row, col, 'D');
			assertTrue(Arrays.deepEquals(resultMapD, testMap));
		}
		if(resultMapW != null) {
			testMap = CopyMap(testMapOri);
			sokoban.moveBox(testMap, row, col, 'W');
			assertTrue(Arrays.deepEquals(resultMapW, testMap));
		}
		if(resultMapS != null) {
			testMap = CopyMap(testMapOri);
			sokoban.moveBox(testMap, row, col, 'S');
			assertTrue(Arrays.deepEquals(resultMapS, testMap));
		}
		if(resultMapA != null) {
			testMap = CopyMap(testMapOri);
			sokoban.moveBox(testMap, row, col, 'A');
			assertTrue(Arrays.deepEquals(resultMapA, testMap));
		}

		System.out.println("\tPassed");
	}

	private char[][] CopyMap(char[][] map){
		char[][] copy = new char[map.length][];
		for(int i = 0; i < map.length; i++){
			copy[i] = map[i].clone();
		}
		return copy;
	}
	@Test
	public void movePlayerTest01(){
		System.out.println("movePlayer() method map test: ");
		movePlayerTest("./testMaps/movePlayer/01", 5, 3);
	}

	@Test
	public void movePlayerTest02(){
		System.out.println("movePlayer() method map test: ");
		movePlayerTest("./testMaps/movePlayer/02", 2, 3);
	}

	@Test
	public void movePlayerTest03(){
		System.out.println("movePlayer() method 02 test: ");
		movePlayerTest("./testMaps/movePlayer/03", 1, 1);
	}

	@Test
	public void movePlayerTest04(){
		System.out.println("movePlayer() method 04 test: ");
		movePlayerTest("./testMaps/movePlayer/04", 6, 5);
	}

	private void movePlayerTest(String path, int row, int col){
		Sokoban sokoban = new Sokoban();

		char[][] testMapOri = readMap(path + "/ori");
		char[][] resultMapD = readMap(path + "/D");
		char[][] resultMapW = readMap(path + "/W");
		char[][] resultMapS = readMap(path + "/S");
		char[][] resultMapA = readMap(path + "/A");

		char[][] testMap;
		if(resultMapD != null){
			testMap = CopyMap(testMapOri);
			sokoban.movePlayer(testMap, row, col, 'D');
			assertTrue(Arrays.deepEquals(resultMapD, testMap));
		}
		if(resultMapW != null) {
			testMap = CopyMap(testMapOri);
			sokoban.movePlayer(testMap, row, col, 'W');
			assertTrue(Arrays.deepEquals(resultMapW, testMap));
		}
		if(resultMapS != null) {
			testMap = CopyMap(testMapOri);
			sokoban.movePlayer(testMap, row, col, 'S');
			assertTrue(Arrays.deepEquals(resultMapS, testMap));
		}
		if(resultMapA != null) {
			testMap = CopyMap(testMapOri);
			sokoban.movePlayer(testMap, row, col, 'A');
			assertTrue(Arrays.deepEquals(resultMapA, testMap));
		}

		System.out.println("\tPassed");
	}

	@Test
	public void gameOverTest01(){
		System.out.println("gameOver() method test negative map: ");
		Sokoban sokoban = new Sokoban();
		char[][] map = readMap("./testMaps/gameOver/neg/01");
		assertFalse(sokoban.gameOver(map));
		System.out.println("\tPassed");
	}

	@Test
	public void gameOverTest02(){
		System.out.println("gameOver() method test negative map: ");
		Sokoban sokoban = new Sokoban();
		char[][] map = readMap("./testMaps/gameOver/neg/02");
		assertFalse(sokoban.gameOver(map));
		System.out.println("\tPassed");
	}

	@Test
	public void gameOverTest03(){
		System.out.println("gameOver() method test negative 02: ");
		Sokoban sokoban = new Sokoban();
		char[][] map = readMap("./testMaps/gameOver/neg/03");
		assertFalse(sokoban.gameOver(map));
		System.out.println("\tPassed");
	}

	@Test
	public void gameOverTest04(){
		System.out.println("gameOver() method test positive map: ");
		Sokoban sokoban = new Sokoban();
		char[][] map = readMap("./testMaps/gameOver/pos/01");
		assertTrue(sokoban.gameOver(map));
		System.out.println("\tPassed");
	}

	@Test
	public void gameOverTest05(){
		System.out.println("gameOver() method test positive map: ");
		Sokoban sokoban = new Sokoban();
		char[][] map = readMap("./testMaps/gameOver/pos/02");
		assertTrue(sokoban.gameOver(map));
		System.out.println("\tPassed");
	}

	@Test
	public void gameOverTest06(){
		System.out.println("gameOver() method test positive 02: ");
		Sokoban sokoban = new Sokoban();
		char[][] map = readMap("./testMaps/gameOver/pos/03");
		assertTrue(sokoban.gameOver(map));
		System.out.println("\tPassed");
	}

	@Test
	public void numberOfRowsTest01(){
		System.out.println("numberOfRows() method 01: ");
		Sokoban sokoban = new Sokoban();
		assertEquals(11, sokoban.numberOfRows("testMaps/readMap/01/map"));
		System.out.println("\tPassed");
	}

	@Test
	public void numberOfRowsTest02(){
		System.out.println("numberOfRows() method 01: ");
		Sokoban sokoban = new Sokoban();
		assertEquals(10, sokoban.numberOfRows("testMaps/readMap/02/map"));
		System.out.println("\tPassed");
	}

	@Test
	public void readmapTest01(){
		System.out.println("readmap() method test 01: ");
		readmapTest("testMaps/readMap/01/map");
	}

	@Test
	public void readmapTest02(){
		System.out.println("readmap() method test 01: ");
		readmapTest("testMaps/readMap/02/map");
	}


	private void readmapTest(String path){
		Sokoban sokoban = new Sokoban();
		char[][] map = sokoban.readmap(path);
		char[][] expected = readMap(path);

		for(int i = 0; i < Math.min(map.length, expected.length); i++){
			for(int j = 0; j < Math.min(map[i].length, expected[i].length); j++){
				assertEquals(expected[i][j], map[i][j]);
			}
		}
		System.out.println("\tPassed");
	}


	@Test
	public void findPlayerTest01(){
		System.out.println("findPlayer() method test 01: ");
		findPlayerTest("testMaps/readMap/01/map", new int[]{2, 2});
	}

	@Test
	public void findPlayerTest02(){
		System.out.println("findPlayer() method test 01: ");
		findPlayerTest("testMaps/readMap/02/map", new int[]{5, 16});
	}

	@Test
	public void findPlayerTest03(){
		System.out.println("findPlayer() method test 02: ");
		findPlayerTest("testMaps/readMap/03/map", new int[]{4, 5});
	}

	private void findPlayerTest(String path, int[] expected){
		System.out.println("Testing findPlayer() method: ");
		Sokoban sokoban = new Sokoban();

		int[] result = sokoban.findPlayer(readMap(path));
		assertArrayEquals(expected, result);
		System.out.println("\tPassed");
	}
	@Test
	public void isValidTest01(){
		System.out.println("isValid() method test 01: ");
		isValidTest("testMaps/isValid/A", 'A');
	}

	@Test
	public void isValidTest02(){
		System.out.println("isValid() method test 02: ");
		isValidTest("testMaps/isValid/D", 'D');
	}

	@Test
	public void isValidTest03(){
		System.out.println("isValid() method test 03: ");
		isValidTest("testMaps/isValid/S", 'S');
	}

	@Test
	public void isValidTest04(){
		System.out.println("isValid() method test 04: ");
		isValidTest("testMaps/isValid/W", 'W');
	}

	private void isValidTest(String path, char direction){
		Sokoban sokoban = new Sokoban();

		char[][][] negs;
		char[][][] poss;

		File negFolder = new File(path + "/neg");
		File[] negFiles = negFolder.listFiles();
		negs = new char[negFiles.length][][];
		for (int i = 0; i < negFiles.length; i++) {
			negs[i] = readMap(negFiles[i].getPath());
		}

		File posFolder = new File(path + "/pos");
		File[] posFiles = posFolder.listFiles();
		poss = new char[posFiles.length][][];
		for (int i = 0; i < posFiles.length; i++) {
			poss[i] = readMap(posFiles[i].getPath());
		}

		//test all the maps in negs and poss:
		for (int i = 0; i < negs.length; i++) {
			int[] player = findPlayer(negs[i]);
			assertFalse(sokoban.isValid(negs[i], player[0], player[1], direction));
		}

		for (int i = 0; i < poss.length; i++) {
			int[] player = findPlayer(poss[i]);
			assertTrue(sokoban.isValid(poss[i], player[0], player[1], direction));
		}
		System.out.println("\tPassed");
	}

	public int[] findPlayer(char[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] == 'o') {
					return new int[] { i, j };
				}
			}
		}
		return null;
	}

	@ Test
	public void printMapTest01(){
		System.out.println("printMap() method test 01: ");
		printMapTest("testMaps/readMap/01");
	}

	@ Test
	public void printMapTest02(){
		System.out.println("printMap() method test 02: ");
		printMapTest("testMaps/readMap/02");
	}

	@ Test
	public void printMapTest03(){
		System.out.println("printMap() method test 03: ");
		printMapTest("testMaps/readMap/03");
	}

	@ Test
	public void printMapTest04(){
		System.out.println("printMap() method test 04: ");
		printMapTest("testMaps/readMap/04");
	}

	private void printMapTest(String path){
		Sokoban sokoban = new Sokoban();

		char[][] testMap = readMap(path + "/map");
		String expected = mapToString(readMap(path + "/print"));
		sokoban.printMap(testMap);
		// save the original output streams
		PrintStream originalOut = System.out;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));

		sokoban.printMap(testMap);

		System.setOut(originalOut);

		assertEquals(expected, baos.toString());
		System.out.println("\tPassed");
	}

	private String mapToString(char[][] map) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < map.length; i++) {
			result.append(new String(map[i])).append("\n");
		}
		return result.toString();
	}
}