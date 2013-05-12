package com.thuf.hangman;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.thuf.hangman.GameEngine.CheatEngine;

public class TestGameEngine {
	private GameEngine engine;
	private String word = "Hangman";

	@Before
	public void setUp() throws Exception {
		engine = new GameEngine(word);
	}

	@Test
	public void testGameEngineRevealLetters() throws Exception {
		assertTrue(engine.revealLetters('H'));
		assertTrue(engine.revealLetters('h'));
		assertTrue(engine.revealLetters('a'));
	}

	@Test
	public void testGameEngineRevealLettersWithWrongLetters() throws Exception {
		assertFalse(engine.revealLetters('e'));
		assertFalse(engine.revealLetters('Z'));
		assertFalse(engine.revealLetters('x'));
	}

	@Test
	public void testGameEngineGameWord() throws Exception {
		assertEquals("*******", engine.getGameWord());
		assertReveal('c', "*******", false);
		assertReveal('h', "H******", false);
		assertReveal('H', "H******", false);
		assertReveal('a', "HA***A*", false);
		assertReveal('N', "HAN**AN", false);
		assertReveal('G', "HANG*AN", false);
		assertReveal('m', "HANGMAN", true);
	}

	private void assertReveal(char c, String expectedWord, boolean expectedWinning) {
		engine.revealLetters(c);
		String msg = "Word shoud be " + expectedWord + ", but was " + engine.getGameWord();
		assertEquals(msg, expectedWord, engine.getGameWord());
		assertEquals(expectedWinning, engine.isWinning());
	}

	@Test
	public void testCheatEngine() throws Exception {
		CheatEngine cheat = new CheatEngine(engine);
		assertCheat(cheat, true, "H******", false);
		assertCheat(cheat, true, "HA***A*", false);
		assertCheat(cheat, true, "HAN**AN", false);
		assertCheat(cheat, true, "HANG*AN", false);
		assertCheat(cheat, true, "HANGMAN", true);
	}

	private void assertCheat(CheatEngine cheat, boolean expectedCheat, String expectedWord,
			boolean expectedWinning) {
		assertEquals(expectedCheat, cheat.cheat());
		String msg = "Word shoud be " + expectedWord + ", but was " + engine.getGameWord();
		assertEquals(msg, expectedWord, engine.getGameWord());
		assertEquals(expectedWinning, engine.isWinning());
	}

	@Test
	public void testCheatEngineWith3Cheats() throws Exception {
		CheatEngine cheat = new CheatEngine(engine, 3);
		assertCheat(cheat, true, "H******", false);
		assertCheat(cheat, true, "HA***A*", false);
		assertCheat(cheat, true, "HAN**AN", false);
		assertCheat(cheat, false, "HAN**AN", false);
		assertCheat(cheat, false, "HAN**AN", false);
	}

	@Test
	public void testGameEngineAndCheatEngine() throws Exception {
		CheatEngine cheat = new CheatEngine(engine, 2);
		assertReveal('A', "*A***A*", false);
		assertCheat(cheat, true, "HA***A*", false);
		assertReveal('N', "HAN**AN", false);
		assertCheat(cheat, true, "HANG*AN", false);
		assertCheat(cheat, false, "HANG*AN", false);
		assertCheat(cheat, false, "HANG*AN", false);
		assertReveal('M', "HANGMAN", true);
	}

	@Test
	public void testLosingGame() throws Exception {
		assertRevealLosing('H', true, "H******", false);
		assertRevealLosing('A', true, "HA***A*", false);

		assertRevealLosing('Z', false, "HA***A*", false);
		assertRevealLosing('D', false, "HA***A*", false);
		assertRevealLosing('F', false, "HA***A*", false);
		assertRevealLosing('R', false, "HA***A*", false);
		assertRevealLosing('T', false, "HA***A*", false);
		assertRevealLosing('W', false, "HA***A*", false);
		assertRevealLosing('P', false, "HA***A*", true);
	}

	private void assertRevealLosing(char c, boolean expectedRevealed, String expectedWord,
			boolean expectedLosing) {
		assertEquals(expectedRevealed, engine.revealLetters(c));
		assertEquals(expectedWord, engine.getGameWord());
		assertEquals(expectedLosing, engine.isLosing());
	}
}
