package app.dayflow.journal;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

/** Builds both widgets from the summary the Dayflow page last sent. */
final class WidgetUtil {
    static final String PREFS = "dayflow";
    static final String KEY = "summary";

    private WidgetUtil() { }

    static void updateAll(Context ctx) {
        AppWidgetManager m = AppWidgetManager.getInstance(ctx);
        for (int id : m.getAppWidgetIds(new ComponentName(ctx, MediumWidget.class))) m.updateAppWidget(id, build(ctx, true));
        for (int id : m.getAppWidgetIds(new ComponentName(ctx, SmallWidget.class))) m.updateAppWidget(id, build(ctx, false));
    }

    static RemoteViews build(Context ctx, boolean medium) {
        RemoteViews v = new RemoteViews(ctx.getPackageName(), medium ? R.layout.widget_medium : R.layout.widget_small);
        String raw = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, null);

        Calendar now = Calendar.getInstance();
        String today = String.format(Locale.US, "%04d-%02d-%02d", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int dow = (now.get(Calendar.DAY_OF_WEEK) + 5) % 7;   // Monday = 0, like the web app

        int done = 0, total = 0, hp = 100, level = 1;
        String mood = "content", petName = "Chomp", cdTitle = "", cdEmoji = "";
        long cdTarget = 0;
        boolean fainted = false, hasData = raw != null;

        if (hasData) {
            try {
                JSONObject j = new JSONObject(raw);
                petName = j.optString("petName", petName);
                hp = j.optInt("petHp", 100);
                level = j.optInt("petLevel", 1);
                fainted = j.optBoolean("petFainted", false);
                cdTitle = j.optString("cdTitle", "");
                cdEmoji = j.optString("cdEmoji", "");
                cdTarget = j.optLong("cdTarget", 0);
                if (today.equals(j.optString("date"))) {
                    done = j.optInt("done", 0);
                    total = j.optInt("total", 0);
                    mood = j.optString("petMood", "content");
                } else {
                    // A new day started since the app was last opened: today's habits are all still to do
                    JSONArray t = j.optJSONArray("totalsByDow");
                    total = t != null && t.length() == 7 ? t.optInt(dow, 0) : 0;
                    done = 0;
                    mood = fainted ? "fainted" : "content";
                }
            } catch (Exception ignored) { hasData = false; }
        }
        if (!fainted && (hour >= 23 || hour < 6)) mood = "sleep";
        if (fainted) mood = "fainted";

        v.setImageViewResource(R.id.w_pet, petImage(mood));

        String habits;
        if (!hasData) habits = "Open Dayflow";
        else if (total == 0) habits = "Rest day";
        else habits = done + "/" + total + (medium ? " habits done" : "");
        v.setTextViewText(R.id.w_habits, habits);
        v.setProgressBar(R.id.w_bar, 100, total == 0 ? (hasData ? 100 : 0) : Math.round(100f * done / total), false);
        v.setTextViewText(R.id.w_hp, "❤️ " + hp);

        if (medium) {
            v.setTextViewText(R.id.w_title, petName + " · Lv " + level + " · " + moodWord(mood));
            if (cdTarget > 0) {
                long ms = cdTarget - System.currentTimeMillis();
                String cd;
                if (ms <= 0) cd = cdEmoji + " " + cdTitle + " · today!";
                else {
                    long days = ms / 86_400_000L;
                    cd = cdEmoji + " " + days + (days == 1 ? " day" : " days") + " · " + cdTitle;
                }
                v.setTextViewText(R.id.w_cd, cd.trim());
                v.setViewVisibility(R.id.w_cd, View.VISIBLE);
            } else {
                v.setViewVisibility(R.id.w_cd, View.GONE);
            }
            v.setOnClickPendingIntent(R.id.w_pet, open(ctx, "pet", 11));
            v.setOnClickPendingIntent(R.id.w_habits_box, open(ctx, "habits", 12));
            v.setOnClickPendingIntent(R.id.w_cd, open(ctx, "countdowns", 13));
            v.setOnClickPendingIntent(R.id.w_root, open(ctx, "home", 14));
        } else {
            v.setOnClickPendingIntent(R.id.w_root, open(ctx, "habits", 21));
        }
        return v;
    }

    private static int petImage(String mood) {
        switch (mood) {
            case "happy": return R.drawable.pet_happy;
            case "sad": return R.drawable.pet_sad;
            case "sleep": return R.drawable.pet_sleep;
            case "fainted": return R.drawable.pet_fainted;
            default: return R.drawable.pet_content;
        }
    }

    private static String moodWord(String mood) {
        switch (mood) {
            case "happy": return "stuffed!";
            case "sad": return "hungry";
            case "sleep": return "asleep";
            case "fainted": return "fainted";
            default: return "content";
        }
    }

    private static PendingIntent open(Context ctx, String tab, int code) {
        Intent i = new Intent(ctx, MainActivity.class);
        i.putExtra("tab", tab);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(ctx, code, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
