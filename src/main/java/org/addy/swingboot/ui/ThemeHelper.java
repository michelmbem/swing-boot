package org.addy.swingboot.ui;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.Theme;
import com.github.weisj.darklaf.theme.spec.ColorToneRule;
import com.github.weisj.darklaf.theme.spec.ContrastRule;
import com.github.weisj.darklaf.theme.spec.PreferredThemeStyle;
import lombok.experimental.UtilityClass;
import org.addy.simpletable.SimpleTable;
import org.addy.swing.KnownColor;

@UtilityClass
public class ThemeHelper {
    public Theme getSystemTheme() {
        return LafManager.themeForPreferredStyle(getPreferredThemeStyle());
    }

    public boolean isLightTheme() {
        return getPreferredThemeStyle().getColorToneRule() == ColorToneRule.LIGHT;
    }

    public boolean isDarkTheme() {
        return getPreferredThemeStyle().getColorToneRule() == ColorToneRule.DARK;
    }

    public boolean isStandardContrastTheme() {
        return getPreferredThemeStyle().getContrastRule() == ContrastRule.STANDARD;
    }

    public boolean isHighContrastTheme() {
        return getPreferredThemeStyle().getContrastRule() == ContrastRule.HIGH_CONTRAST;
    }

    public void adjustSimpleTableColors(SimpleTable simpleTable) {
        if (isDarkTheme()) {
            simpleTable.setAlternateBackground(KnownColor.DARK_BLUE_GREY);
            simpleTable.setRolloverBackground(KnownColor.OLIVE);
        }
    }

    private PreferredThemeStyle getPreferredThemeStyle() {
        return LafManager.getPreferredThemeStyle();
    }
}
