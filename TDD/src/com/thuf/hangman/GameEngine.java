package com.thuf.hangman;

public class GameEngine {
	public static class CheatEngine {
		private final GameEngine engine;
		private final int maxCheats;
		private int usedCheats;

		public CheatEngine(GameEngine engine) {
			this(engine, Integer.MIN_VALUE);
		}

		public CheatEngine(GameEngine engine, int maxCheats) {
			this.engine = engine;
			this.maxCheats = maxCheats;
		}

		public boolean cheat() {
        // Some comment
			int index = engine.gameWord.indexOf("*");
			usedCheats++;
			boolean canCheat = usedCheats <= maxCheats;
			if (index != -1 && canCheat) {
				char c = engine.word.charAt(index);
				engine.revealLetters(c);
			}
			return canCheat;
		}
	}

	private static final int MAX_WRONG_LETTERS = 7;
	private final String word;
	private StringBuilder gameWord;
	private int wrongLetters;

	public GameEngine(String word) {
		this.word = word.toUpperCase();
		gameWord = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			gameWord.append("*");
		}
	}

	public boolean revealLetters(char c) {
		if (Character.isLowerCase(c)) {
			c = Character.toUpperCase(c);
		}
		boolean revealed = word.indexOf(c) != -1;
		if (revealed) {
			int index = -1;
			while ((index = word.indexOf(c, index + 1)) != -1) {
				gameWord.setCharAt(index, Character.toUpperCase(c));
			}
		} else {
			wrongLetters++;
		}
		return revealed;
	}

	public String getGameWord() {
		return gameWord.toString();
	}

	public boolean isWinning() {
		return gameWord.toString().equals(word) && !isLosing();
	}

	public boolean isLosing() {
		return wrongLetters >= MAX_WRONG_LETTERS;
	}
}
