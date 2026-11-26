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

A helper for "give the most niche answer" games. Type the question or paste a screenshot of it, and Claude reads it and suggests real answers that few players will give:

- **Primordial picks**: rarest answers that still clearly fit
- **Deep cuts** and **Safe niche**: progressively safer options
- **Avoid**: what everyone else will say

Each suggestion is checked against Wikipedia page views (sorted rarest first, and flagged if there is no page). Tap an answer to copy it.

Needs your own Anthropic API key (console.anthropic.com), entered once on the page. It is stored only in your browser and sent only to Anthropic. Each question costs roughly 1–3 cents.

# Candle Crawl 🕯️ (`/trader/`)

A day-trading roguelike where the market never closes. Each in-game day is 24 hours (about 100 seconds of real time). Hit the day's net-worth goal to survive, pick 1 of 3 perks, and push as deep as you can.

- Candlestick or line charts, with buy/sell markers and your average entry price
- An earnings calendar posted each morning, with whisper hints before each report
- Boss days every 5th day (The Whale, Flash Crash, Circuit Breaker, Bear Lord) that guarantee a legendary perk
- 17 perks, including short selling, margin, stop-loss bot, insider tips and extra lives
- Wax earned every run buys permanent Trading Desk upgrades (starting cash, rerolls, head-start perk, better whispers, a 4th perk choice)
- Sound effects, haptics, floating P/L numbers and confetti

# Holo Pull ✨ (`/cards/`)

An incremental anime card-collecting game. Character art and names load live from the AniList API (top ~400 characters by fan favorites; nothing is stored in this repo). More-loved characters are rarer: Common → Rare → Epic → Legendary → Mythic.

- Tap for coins; cards earn coins every second (and half-rate while you're away, up to 8 hours)
- Cinematic rare-pull cutscenes for Legendary and Mythic cards: eerie typewriter text, a star flare, an escalating "1 in N" rarity climb, the name in neon rings, a collapse, and a "YOU FOUND" explosion with the card's real odds (tap to skip)
- Four packs with a shake → tear → flip opening animation, particles, light rays, screen shake and sounds; better pulls shake the pack harder
- Holographic glass card design: rarity borders (clear glass, silver foil, holo, gold foil, prismatic), foil that follows your pointer, film-grain noise, frosted info bars
- Editions (Holo, Silver Foil, Gold Foil, Liquid Glass, Prismatic, Glitch, Negative, Shadow) and traits (Signed, 1st Edition, Full Art, Die-cut Sticker, Sparkle, Misprint) multiply a card's value
- Crinkled holographic foil packs that tilt with your pointer
- Drag-to-spin 3D card viewer with holo light that follows the angle
- Upgrades: Power Tap, Fan Club, Lucky Charm, Trait Hunter, Auto-Opener
- Ascend: fuse spare copies to level a character up to Lv5 (+50% income per level)
- Pity meters: a guaranteed Legendary+ within 100 cards and Mythic within 400
- Series sets: own every character from a show for +5% income per completed set
- A free Premium pack every 4 hours, ×5 multi-opens, favorites with a Showcase, 14 achievements and collection search

# Dayflow Journal 📓 (`/journal/`)

A productivity journal with a dark, neon-card look.

- **Countdowns**: count down to any date and time with live days / hours / minutes / seconds and a progress track
- **Habit tracker**: create habits with an icon, color and the weekdays they repeat on; tick them off (including past days), see streaks, best streaks, 30-day rate and a 15-week heatmap
- **Daily journal**: mood picker, top-3 focus tasks you can check off, notes and gratitude, autosaved; a 14-day mood chart and past entries
- **Home dashboard**: today's progress, next countdown, today's habits, journal prompt and stats
- Everything is stored in your browser; download or restore a backup from Settings
