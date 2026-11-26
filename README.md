# Earshot 🎧

A browser game: guess the song from a tiny clip.

- You hear **1 second** first. Can't get it? Press **Skip** to unlock **2s**, then **5s**, then **10s**.
- Type your answer in the answer box. Suggestions pop up as you type; pick one with the arrow keys and Enter, or just type the title.
- A wrong guess also unlocks the next clip length.
- Scoring: 1s = 100, 2s = 75, 5s = 50, 10s = 25. Each game is 10 songs, and your best score is saved per playlist.
- Pick a built-in playlist (Pop, 2000s, Rock, 80s, Hip-Hop & R&B) or type any artist's name.

Song previews come from the free iTunes Search API, so you need an internet connection to play.

## Play

It's a single file with no build step. Either:

- download `index.html` and open it in your browser, or
- turn on GitHub Pages (repo **Settings → Pages → Deploy from a branch**) and open the URL it gives you.

# DeepCut 🔎 (`/niche/`)

A helper for "give the most niche answer" games. Paste the question (e.g. "Name a fruit") and it:

1. pulls every answer it can find from Wikipedia categories, "List of…" pages, and the Datamuse word database,
2. ranks them by popularity (Wikipedia page views over 30 days, word frequency),
3. shows **Primordial picks** (rarest but still real), **Deep cuts**, **Safe niche**, and the popular answers to avoid.

Tap an answer to copy it.
