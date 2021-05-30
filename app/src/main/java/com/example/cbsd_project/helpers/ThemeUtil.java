package com.example.cbsd_project.helpers;

import android.content.Context;

import com.example.cbsd_project.R;

public class ThemeUtil {
    public enum Theme {
        Default(R.string.theme_default),
        Lime(R.string.theme_lime),
        Red(R.string.theme_red),
        Green(R.string.theme_green),
        Blue(R.string.theme_blue),
        ;
        public final int label;

        Theme(int label) {
            this.label = label;
        }
    }

    public static Theme CurrentTheme = Theme.Default;

    public static void setTheme(Context context) {
        // context.getApplicationContext().getTheme().applyStyle(R.style.OverlayThemeDefault, true);
        switch (CurrentTheme) {
            case Lime:
                context.getTheme().applyStyle(R.style.OverlayThemeLime, true);
                context.setTheme(R.style.OverlayThemeLime);
                break;
            case Red:
                context.getTheme().applyStyle(R.style.OverlayThemeRed, true);
                context.setTheme(R.style.OverlayThemeRed);
                break;
            case Green:
                context.getTheme().applyStyle(R.style.OverlayThemeGreen, true);
                context.setTheme(R.style.OverlayThemeGreen);
                break;
            case Blue:
                context.getTheme().applyStyle(R.style.OverlayThemeBlue, true);
                context.setTheme(R.style.OverlayThemeBlue);
                break;
            case Default:
                context.setTheme(R.style.OverlayThemeDefault);
                break;
        }
    }

    public static void setCurrentTheme(Theme theme) {
        ThemeUtil.CurrentTheme = theme;
    }

}
